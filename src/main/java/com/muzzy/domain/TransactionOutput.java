package com.muzzy.domain;

import com.muzzy.cipher.StringUtil;
import lombok.Getter;
import lombok.Setter;

import java.security.PublicKey;

@Getter
@Setter
public class TransactionOutput implements Comparable<TransactionOutput>{

    public String id;
    public PublicKey receiver;
    public float value;
    public String parentTransactionId;


    public TransactionOutput(PublicKey receiver, float value, String parentTransactionId) {
        this.receiver = receiver;
        this.value = value;
        this.parentTransactionId = parentTransactionId;
        this.id = StringUtil.applySha256(StringUtil.getStringFromKey(receiver)+ value +parentTransactionId);
    }


    public boolean isMine(PublicKey publicKey) {
        return publicKey == receiver;
    }


    @Override
    public int compareTo(TransactionOutput o) {
        return Float.compare(this.value, o.value);
    }
}