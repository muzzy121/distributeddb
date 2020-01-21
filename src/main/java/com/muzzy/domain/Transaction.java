package com.muzzy.domain;

import com.muzzy.cipher.StringUtil;
import com.muzzy.service.controllerservice.test.Validation;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

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

    public void generateSignature(PrivateKey privateKey) {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(receiver) + value;
        this.signature = Validation.confirm(privateKey, data);
    }

    public boolean verifiySignature() {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(receiver) + value;
        return Validation.verifySignature(sender, data, signature);
    }


}
