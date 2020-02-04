package com.muzzy.domain;

import com.muzzy.cipher.StringUtil;
import com.muzzy.domain.spsfl.SerialVersionUIDContainer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.security.PublicKey;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionOutput implements Comparable<TransactionOutput>, Serializable {

    private static final long serialVersionUID = SerialVersionUIDContainer.TRANSACTION_OUTPUT_SVUID;


    private String id;
    private PublicKey receiver;
    private float value;
    private String parentTransactionId;


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