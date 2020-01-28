package com.muzzy.service.factory;

import com.muzzy.cipher.StringUtil;
import com.muzzy.domain.Transaction;
import com.muzzy.domain.TransactionInput;
import com.muzzy.domain.TransactionOutput;
import com.muzzy.service.TransactionOutputService;
import com.muzzy.service.controllerservice.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class TransactionFactory {
    private Transaction t;
    private TransactionOutputService transactionOutputService;

    @Autowired
    public TransactionFactory(TransactionOutputService transactionOutputService) {
        this.transactionOutputService = transactionOutputService;
    }

    public Transaction getTransaction(PrivateKey privateKey, PublicKey sender, PublicKey receiver, float value) {
        /**
         * Ten early exit także trzeba obgadać, chyba że poszukać transakcji która pozwoli zapłacić z całości!?
         * Co będzie wydajniejsze - czy lepiej eliminować małe transackcje z UTXO, czy lepiej wydawać z jednego inputa
         */
        if (transactionOutputService.getBalance(sender) < value) {
            System.out.println("You don't have coins for this transaction");
            return null;
        }
        /**
         * Sortowanie listy, trzeba się zastanowić czy jest ono potrzebne i jak wpływana na wydajność
         */
        List<TransactionOutput> transactionOutputList = new ArrayList<TransactionOutput>();
        transactionOutputService.getTransctionByPublicKey(sender).forEach(transactionOutput -> transactionOutputList.add(transactionOutput));
        Collections.sort(transactionOutputList);

        // Mandatory obj.
        ArrayList<TransactionInput> inputs = new ArrayList<>();
        float total = 0F;


        // TODO: 2020-01-28 Trzeba sie zastanowić jak to ma działać
        for (TransactionOutput utxo : transactionOutputList
        ) {
            inputs.add(new TransactionInput(utxo.getId(), utxo));
            total += utxo.getValue();
            if (total >= value) break;
        }

        t = new Transaction().builder().sender(sender).reciever(receiver).value(value).inputs(inputs).build();
        t.setSignature(generateSignature(privateKey));
//        if((!previousHash.equals("0"))) {
            float change = getInputsValue() - value;
            t.setTransactionId(calculateHash());
            t.getOutputs().add(new TransactionOutput(receiver, value, t.getTransactionId()));
            t.getOutputs().add(new TransactionOutput(sender, change, t.getTransactionId()));
            t.getOutputs().forEach(o -> transactionOutputService.save(o));
            inputs.stream().filter(i -> i.getUtxo() != null).forEach(y -> transactionOutputService.deleteById(y.getUtxo().getId()));
//        }

//        return true;
        return t;
    }
    /**
     * Generates Hash using Public Keys and transaction value
     * @return Hash
     */
    private String calculateHash() {
        return StringUtil.applySha256(
                StringUtil.getStringFromKey(t.getSender()) +
                        StringUtil.getStringFromKey(t.getReceiver()) +
                        t.getValue()
        );
    }

    /**
     * Generate this Transaction signature using privateKey, (sender, reciver publicKeys and Value of transaction as data)
     *
     * @param privateKey
     */
    public byte[] generateSignature(PrivateKey privateKey) {
        // TODO: 2020-01-23 Czy sygnatura nie powinna być z datą? Może dodać Pole daty do transakcji, jej utworzenia
        String data = StringUtil.getStringFromKey(t.getSender()) + StringUtil.getStringFromKey(t.getReceiver()) + t.getValue();
        return Validation.confirm(privateKey, data);
    }

    /**
     * Checks if signature was create with proper key pair and proper data
     *
     * @return boolean
     */
    public boolean verifiySignature() {
        String data = StringUtil.getStringFromKey(t.getSender()) + StringUtil.getStringFromKey(t.getReceiver()) + t.getValue();
        return Validation.verifySignature(t.getSender(), data, t.getSignature());
    }
    /**
     * Werify if Signature is proper
     * Calculate transaction Hash
     * Creates and adds TransctionOutputs to List
     * Adds new TransactionOutputs to UTXOs
     *
     * @return true when transaction was proceeded success
     */
//    public boolean processTransaction(Transaction t) {
//        // Verify transaction signature
//
//    }

    public float getInputsValue() {
        double total = t.getInputs().stream()
                .filter(input -> input.getUtxo() != null) //Kiedy może dojść do sytuacji kiedy utxo będzie null!?
                .collect(Collectors.summingDouble(x -> x.getUtxo().value));
        return (float) total;
    }

    public float getOutputsValue() {
        double total = t.getOutputs().stream().collect(Collectors.summingDouble(x -> x.value));
        return (float) total;
    }
}
