package com.muzzy.service.map;

import com.muzzy.service.WalletService;
import com.muzzy.service.controllerservice.test.Wallet;

import java.util.Set;

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
