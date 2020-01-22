package com.muzzy.domain;

import com.muzzy.cipher.StringUtil;
import com.muzzy.service.controllerservice.test.Validation;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by Paweł Mazur
 * 14.01.2020
 */

@Getter
@Setter
public class Transaction implements Serializable {
    public String transactionId;
    public PublicKey sender;
    public PublicKey receiver;
    public float value;
    public byte[] signature;


    public ArrayList<TransactionInput> inputs;
    public ArrayList<TransactionOutput> outputs = new ArrayList<>();
    public Transaction(PublicKey sender, PublicKey reciever, float value, ArrayList<TransactionInput> inputs) {
        this.sender = sender;
        this.receiver = reciever;
        this.value = value;
        this.inputs = inputs;
    }

    public Transaction() {
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


    public boolean processTransaction(HashMap<String, TransactionOutput> map) {

        // Verify transaction

        if (!verifiySignature()) {
            System.out.println("Wrong Signature");
            return false;
        }

        // biere transakcje z listy inputs, jeżeli coś w niej jest to z Repo wszystkich transakcji wybieram taką transakcje,
        // która ma id takie jak id obiektu z listy input i przypisuje do pola UTXO tego obiektu -- nie wiem co tu sie dzieje!
//        for (TransactionInput i : inputs) {
//            i.UTXO = map.get(i.transactionOutputId);  // te public-i jeszcze w mapie to masakra
//        }
        inputs.stream().forEach(i -> i.UTXO = map.get(i.transactionOutputId));


        // Róznica miedzy wartością transakcji Input, a waroscią tej transackji w której jestem
        float change = getInputsValue() - value;

        transactionId = calculateHash();  // licze hash transakcji

        outputs.add(new TransactionOutput(this.receiver, value, transactionId)); //dodaje transakcje output do listy outputs w transakcji
        outputs.add(new TransactionOutput(this.sender, change, transactionId)); //dodaje transakcje output do listy outputs w transakcji

        for (TransactionOutput o : outputs) {
            map.put(o.id, o);
        }

        for (TransactionInput i : inputs) {
            if (i.UTXO == null) continue;
            map.remove(i.UTXO.id);
        }

        return true;
    }

    public float getInputsValue() {
//        float total = 0;
//        for (TransactionInput i : inputs) {
//            if (i.UTXO == null) continue;
//            total += i.UTXO.value;
//        }

        double total = inputs.stream()
                .filter(input -> input.UTXO!=null)
                .collect(Collectors.summingDouble(x -> x.UTXO.value));
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
