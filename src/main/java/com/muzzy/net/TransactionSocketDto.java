package com.muzzy.net;

import com.muzzy.domain.Transaction;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@Component
public class TransactionSocketDto implements Serializable, Sendable {
//    private Set<Transaction>
    private Long id;
}
