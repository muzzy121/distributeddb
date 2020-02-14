package com.muzzy.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.muzzy.cipher.StringUtil;
import com.muzzy.domain.spsfl.SerialVersionUIDContainer;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.security.PublicKey;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties("receiverKey")
public class TransactionOutput implements Comparable<TransactionOutput>, Serializable {

    private static final long serialVersionUID = SerialVersionUIDContainer.TRANSACTION_OUTPUT_SVUID;
    private String id;

    private PublicKey receiver;
    private BigDecimal value;
    private String parentTransactionId;


    public PublicKey getReceiverKey(){
        return this.receiver;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public String getReceiver() {
        return receiver.toString();
    }

    public BigDecimal getValue() {
        return value;
    }

    public String getParentTransactionId() {
        return parentTransactionId;
    }

    public TransactionOutput(PublicKey receiver, BigDecimal value, String parentTransactionId) {
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
        return value.compareTo(o.value);
    }
}