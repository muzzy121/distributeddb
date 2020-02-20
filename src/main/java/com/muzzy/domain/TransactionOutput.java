package com.muzzy.domain;

import com.muzzy.cipher.StringUtil;
import com.muzzy.domain.spsfl.SerialVersionUIDContainer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionOutput implements Comparable<TransactionOutput>, Serializable {

    private static final long serialVersionUID = SerialVersionUIDContainer.TRANSACTION_OUTPUT_SVUID;


    private String id;
    private String receiver;
    private BigDecimal value;
    private String parentTransactionId;


    public TransactionOutput(String receiver, BigDecimal value, String parentTransactionId) {
        this.receiver = receiver;
        this.value = value;
        this.parentTransactionId = parentTransactionId;
        this.id = StringUtil.applySha256(receiver + value +parentTransactionId);
    }

    public boolean isMine(String publicKey) {
        return publicKey == receiver;
    }

    @Override
    public int compareTo(TransactionOutput o) {
        return value.compareTo(o.value);
    }
}