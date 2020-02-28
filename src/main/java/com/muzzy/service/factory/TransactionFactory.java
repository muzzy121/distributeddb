package com.muzzy.service.factory;

import com.muzzy.cipher.StringUtil;
import com.muzzy.domain.Transaction;
import com.muzzy.domain.TransactionInput;
import com.muzzy.domain.TransactionOutput;
import com.muzzy.net.api.RESTApiControl;
import com.muzzy.service.TransactionOutputService;
import com.muzzy.service.TransactionTemporarySet;
import com.muzzy.service.controllerservice.Validation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class TransactionFactory {
    private final Logger LOG = LoggerFactory.getLogger(TransactionFactory.class);
    private Transaction transaction;
    private TransactionOutputService transactionOutputService;
    private TransactionTemporarySet transactionTemporarySet;
    private RESTApiControl restApiControl;

    @Autowired
    public TransactionFactory(TransactionOutputService transactionOutputService, TransactionTemporarySet transactionTemporarySet, RESTApiControl restApiControl) {
        this.transactionOutputService = transactionOutputService;
        this.transactionTemporarySet = transactionTemporarySet;
        this.restApiControl = restApiControl;

    }

    public Transaction getTransaction(String privateKey, String sender, String receiver, BigDecimal value) {
        /**
         * Ten early exit także trzeba obgadać, chyba że poszukać transakcji która pozwoli zapłacić z całości!?
         * Co będzie wydajniejsze - czy lepiej eliminować małe transackcje z UTXO, czy lepiej wydawać z jednego inputa
         */
        transactionOutputService.save(restApiControl.getAllUtxo());

        if (transactionOutputService.getBalance(sender).compareTo(value) < 0 ) {
            LOG.error("You don't have coins for this transaction");
            return null;
        }
        /**
         * Sortowanie listy, trzeba się zastanowić czy jest ono potrzebne i jak wpływana na wydajność
         */
        List<TransactionOutput> transactionOutputList = new ArrayList<TransactionOutput>();
        transactionOutputService.getTransctionByReciever(sender).forEach(transactionOutput -> transactionOutputList.add(transactionOutput));
        Collections.sort(transactionOutputList);

        // Mandatory obj.
        ArrayList<TransactionInput> inputs = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;


        // TODO: 2020-01-28 Trzeba sie zastanowić jak to ma działać
        for (TransactionOutput utxo : transactionOutputList
        ) {
            inputs.add(new TransactionInput(utxo.getId(), utxo));
            total.add(utxo.getValue());
            if (total.compareTo(value) >= 0 ) break;
        }

        transaction = new Transaction().builder().sender(sender).receiver(receiver).value(value).inputs(inputs).build();
        transaction.setSignature(generateSignature(privateKey));
//        if((!previousHash.equals("0"))) {
            BigDecimal change = getInputsValue().add(value.negate());
            transaction.setTransactionId(calculateHash());
            transaction.getOutputs().add(new TransactionOutput(receiver, value, transaction.getTransactionId()));
            transaction.getOutputs().add(new TransactionOutput(sender, change, transaction.getTransactionId()));

        // TODO: 2020-02-01 To miejsce jest do zmiany, dodaje do UTXO transakcje które nie zostały jeszcze dodane do bloku
            //można stworzyć małe listy które będą przechowywać poza klasą takie dane
            //Test ficzera, zamieniam UTXO na tymczasową listę, którą przepiszę do właściwej listy po dodaniu bloku do łańcucha

            // spent money wait to add to block
            transaction.getOutputs().stream().filter(transactionOutput -> transactionOutput.getReceiver().equals(receiver)).forEach(t-> transactionTemporarySet.addTransaction(t));
            // redirect change directly to UTXO
            transaction.getOutputs().stream().filter(transactionOutput -> !transactionOutput.getReceiver().equals(receiver)).forEach(t -> transactionOutputService.save(t));
           //Kasowanie starych wejść? Kasowanie bloku ze względu na np. jedną nieprawidłową transakcję spowoduje fraud środków
            inputs.stream().filter(i -> i.getUtxo() != null).forEach(y -> transactionOutputService.deleteById(y.getUtxo().getId()));

        return transaction;
    }
    /**
     * Generates Hash using Public Keys and transaction value
     * @return Hash
     */
    private String calculateHash() {
        return StringUtil.applySha256(
                transaction.getSender() +
                        transaction.getReceiver() +
                        transaction.getValue()
        );
    }

    /**
     * Generate this Transaction signature using privateKey, (sender, reciver publicKeys and Value of transaction as data)
     *
     * @param privateKey
     */
    public byte[] generateSignature(String privateKey) {
        // TODO: 2020-01-23 Czy sygnatura nie powinna być z datą? Może dodać Pole daty do transakcji, jej utworzenia

        String data = transaction.getSender() + transaction.getReceiver() + transaction.getValue();
        return Validation.confirm(privateKey, data);
    }

    /**
     * Checks if signature was create with proper key pair and proper data
     *
     * @return boolean
     */
    public boolean verifySignature() {
        String data = transaction.getSender() + transaction.getReceiver() + transaction.getValue();
        return Validation.verifySignature(transaction.getSender(), data, transaction.getSignature());
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

    public BigDecimal getInputsValue() {
        BigDecimal total = transaction.getInputs().stream()
                .filter(input -> input.getUtxo() != null) //Kiedy może dojść do sytuacji kiedy utxo będzie null!?
                .map(TransactionInput::getUtxo)
                .map(TransactionOutput::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return total;
    }

    public BigDecimal getOutputsValue() {
        BigDecimal total = transaction.getOutputs().stream()
                .map(TransactionOutput::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return total;
    }
}
