package com.muzzy.service.map;

import com.muzzy.service.WalletService;
import com.muzzy.domain.Wallet;
import org.springframework.stereotype.Service;

import java.util.Set;
@Service
public class WalletMapService extends AbstractWalletMapService<Wallet, String> implements WalletService {

    @Override
    public Set<Wallet> getAll() {
        return super.findAll();
    }

    @Override
    public void deleteById(String s) {
        super.deleteById(s);
    }

    @Override
    public void delete(Wallet t) {
        super.delete(t);
    }

    @Override
    public Wallet save(Wallet t) {
        return super.save(t);
    }

    @Override
    public Wallet getById(String s) {
        return super.findById(s);
    }
}
