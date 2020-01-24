package com.muzzy.domain;

import com.muzzy.cipher.StringUtil;
import com.muzzy.service.TransactionOutputService;
import com.muzzy.service.controllerservice.test.Validation;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Created by Paweł Mazur
 * 14.01.2020
 */

@Getter
@Setter

@NoArgsConstructor
public class Transaction implements Serializable {
    private String transactionId;
    private PublicKey sender;
    private PublicKey receiver;
    private float value;
    private byte[] signature;
    private ArrayList<TransactionInput> inputs;
    private ArrayList<TransactionOutput> outputs = new ArrayList<>();


    private TransactionOutputService transactionOutputService;

    @Builder
    public Transaction(PublicKey sender, PublicKey reciever, float value, ArrayList<TransactionInput> inputs,TransactionOutputService transactionOutputService) {
        this.sender = sender;
        this.receiver = reciever;
        this.value = value;
        this.inputs = inputs;
        this.transactionOutputService = transactionOutputService;
    }


    private String calculateHash() {
        return StringUtil.applySha256(
                StringUtil.getStringFromKey(this.sender) +
                        StringUtil.getStringFromKey(this.receiver) +
                        this.value
        );
    }


    /**
     * Generate this Transaction signature using privateKey, (sender, reciver publicKeys and Value of transaction as data)
     * @param privateKey
     */
    public void generateSignature(PrivateKey privateKey) {
        // TODO: 2020-01-23 Czy sygnatura nie powinna być z datą?
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(receiver) + value;
        this.signature = Validation.confirm(privateKey, data);
    }

    /**
     * Checks if signature was create with proper key pair and proper data
     * @return boolean
     */
    public boolean verifiySignature() {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(receiver) + value;
        return Validation.verifySignature(sender, data, signature);
    }


    public boolean processTransaction() {

        // Verify transaction signature

        if (!verifiySignature()) {
            System.out.println("Wrong Signature");
            return false;
        }

        // biere transakcje z listy inputs, jeżeli coś w niej jest to z Repo wszystkich transakcji wybieram taką transakcje,
        // która ma id takie jak id obiektu z listy input i przypisuje do pola UTXO tego obiektu -- nie wiem co tu sie dzieje!
//        for (TransactionInput i : inputs) {
//            i.UTXO = map.get(i.transactionOutputId);  // te public-i jeszcze w mapie to masakra
//        }

        //Dlaczego połowa operacji została zrobiona tam a druga jest robiona tu!?
//        inputs.stream().forEach(i -> i.setUtxo(transactionService.getById(i.getTransactionOutputId())));


        // Reszta
        float change = getInputsValue() - value;

        transactionId = calculateHash();  // licze hash transakcji

        outputs.add(new TransactionOutput(this.receiver, value, transactionId)); //dodaje transakcje output do listy outputs w transakcji
        outputs.add(new TransactionOutput(this.sender, change, transactionId)); //dodaje transakcje output do listy outputs w transakcji

//        for (TransactionOutput o : outputs) {
////            map.put(o.id, o);
//            transactionService.save(o.id,o);
//        }
        outputs.forEach(o -> transactionOutputService.save(o.getId(),o));
        inputs.stream().filter(i -> i.getUtxo() != null).forEach(y -> transactionOutputService.deleteById(y.getUtxo().getId()));

//        for (TransactionInput i : inputs) {
//            if (i.getUtxo() == null) continue;
////            map.remove(i.UTXO.id);
//            transactionService.deleteById(i.getUtxo().getId());
//        }

        return true;
    }

    public float getInputsValue() {
//        float total = 0;
//        for (TransactionInput i : inputs) {
//            if (i.UTXO == null) continue;
//            total += i.UTXO.value;
//        }

        double total = inputs.stream()
                .filter(input -> input.getUtxo()!=null) //Kiedy może dojść do sytuacji kiedy utxo będzie null!?
                .collect(Collectors.summingDouble(x -> x.getUtxo().value));
        return (float) total;
    }

    public float getOutputsValue() {
//        float total = 0;
//        for (TransactionOutput o : outputs) {
//            total += o.value;
//        }

        double total = outputs.stream().collect(Collectors.summingDouble(x->x.value));
        return (float) total;
    }

}
