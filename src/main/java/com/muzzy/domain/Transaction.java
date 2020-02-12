package com.muzzy.domain;

import com.muzzy.domain.spsfl.SerialVersionUIDContainer;
import com.muzzy.service.TransactionOutputService;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.security.PublicKey;
import java.util.ArrayList;

/**
 * Created by Paweł Mazur
 * 14.01.2020
 */

@Getter
@Setter
@NoArgsConstructor
public class Transaction implements Serializable {

    private static final long serialVersionUID = SerialVersionUIDContainer.TRANSACTION_SVUID;

    private String transactionId;
    private PublicKey sender;
    private PublicKey receiver;
    private float value;
    private byte[] signature;
    private ArrayList<TransactionInput> inputs;
    private ArrayList<TransactionOutput> outputs = new ArrayList<>();

    @Autowired
    private TransactionOutputService transactionOutputService;

    @Builder
    public Transaction(PublicKey sender, PublicKey receiver, float value, ArrayList<TransactionInput> inputs, ArrayList<TransactionOutput> outputs, String transactionId) {
        this.sender = sender;
        this.receiver = receiver;
        this.value = value;
        this.inputs = inputs;
//        this.outputs = outputs;
        this.transactionId = transactionId;
//        // TODO: 2020-01-23 Czy sygnatura nie powinna być z datą? Może dodać Pole daty do transakcji, jej utworzenia
    }

//    /**
//     * Generates Hash using Public Keys and transaction value
//     *
//     * @return Hash
//     */
//    private String calculateHash() {
//        return StringUtil.applySha256(
//                StringUtil.getStringFromKey(this.sender) +
//                        StringUtil.getStringFromKey(this.receiver) +
//                        this.value
//        );
//    }
//
//    /**
//     * Generate this Transaction signature using privateKey, (sender, reciver publicKeys and Value of transaction as data)
//     *
//     * @param privateKey
//     */
//    public void generateSignature(PrivateKey privateKey) {
//        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(receiver) + value;
//        this.signature = Validation.confirm(privateKey, data);
//    }
//
//    /**
//     * Checks if signature was create with proper key pair and proper data
//     *
//     * @return boolean
//     */
//    public boolean verifiySignature() {
//        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(receiver) + value;
//        return Validation.verifySignature(sender, data, signature);
//    }
//
//    /**
//     * Werify if Signature is proper
//     * Calculate transaction Hash
//     * Creates and adds TransctionOutputs to List
//     * Adds new TransactionOutputs to UTXOs
//     *
//     * @return true when transaction was proceeded success
//     */
//    public boolean processTransaction() {
//        // Verify transaction signature
//        if (!verifiySignature()) {
//            System.out.println("Wrong Signature");
//            return false;
//        }
//
//        float change = getInputsValue() - value;
//        transactionId = calculateHash();
//        outputs.add(new TransactionOutput(this.receiver, value, transactionId));
//        outputs.add(new TransactionOutput(this.sender, change, transactionId));
//        outputs.forEach(o -> transactionOutputService.save(o));
//        inputs.stream().filter(i -> i.getUtxo() != null).forEach(y -> transactionOutputService.deleteById(y.getUtxo().getId()));
//        return true;
//    }
//
//    public float getInputsValue() {
//        double total = inputs.stream()
//                .filter(input -> input.getUtxo() != null) //Kiedy może dojść do sytuacji kiedy utxo będzie null!?
//                .collect(Collectors.summingDouble(x -> x.getUtxo().value));
//        return (float) total;
//    }
//
//    public float getOutputsValue() {
//        double total = outputs.stream().collect(Collectors.summingDouble(x -> x.value));
//        return (float) total;
//    }
}