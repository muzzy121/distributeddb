package com.muzzy.service.controllerservice;


import com.muzzy.cipher.StringUtil;
import com.muzzy.domain.Block;
import com.muzzy.domain.Transaction;
import com.muzzy.domain.TransactionOutput;

import java.security.Signature;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

public class Validation {

    public static byte[] confirm(String privateKey, String input) {
        byte[] output;
        try {
            Signature confirmation = Signature.getInstance("SHA256withRSA");
            confirmation.initSign(StringUtil.getPrvKeyFromString(privateKey));
            byte[] strByte = input.getBytes();
            confirmation.update(strByte);
            output = confirmation.sign();
            return output;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean verifySignature(String publicKey, String data, byte[] signature) {
        try {
            Signature verify = Signature.getInstance("SHA256withRSA");
            verify.initVerify(StringUtil.getPubKeyFromString(publicKey));
            verify.update(data.getBytes());
            return verify.verify(signature);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String calculateHash(Block block) {
        String hash = block.getPreviousHash() + block.getTimestamp() + block.getTransactions() + block.getNonce();
        return StringUtil.applySha256(hash);
    }


    public static Boolean isChainValid(Transaction ancestorTransaction, int difficulty, List<Block> blockLinkedHashSet) {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');
        HashMap<String, TransactionOutput> tempUTXOs = new HashMap<>();
        tempUTXOs.put(ancestorTransaction.getOutputs().get(0).getId(), ancestorTransaction.getOutputs().get(0));

        for (int i = 1; i < blockLinkedHashSet.size(); i++) {
            currentBlock = blockLinkedHashSet.get(i);
            previousBlock = blockLinkedHashSet.get(i - 1);

//    return true;
//    }
//}
            if (!currentBlock.getHash().equals(calculateHash(currentBlock))) {
                System.out.println("Current Hashes not equal");
                return false;
            }
        if (!previousBlock.getHash().equals(currentBlock.getPreviousHash())) {
            System.out.println(previousBlock.getHash() + " " + currentBlock.getPreviousHash());
            System.out.println("Previous Hashes not equal");
            return false;
        }
//
//        if (!currentBlock.hash.substring(0, difficulty).equals(hashTarget)) {
//            System.out.println("Mining in progress or malfunction");
//            return false;
//        }
//
//        TransactionOutput tempOutput;
//        for (int j = 0; j < currentBlock.transactions.size(); j++) {
//            Transaction currentTransaction = currentBlock.transactions.get(j);
//
//            if (!currentTransaction.verifiySignature()) {
//                System.out.println("Signature on Transaction(" + j + ") is wrong");
//                return false;
//            }
//            if (currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) {
//                System.out.println("Inputs are note equal to outputs on Transaction(" + j + ")");
//                return false;
//            }
//
//            for (TransactionInput input : currentTransaction.inputs) {
//                tempOutput = tempUTXOs.get(input.transactionOutputId);
//
//                if (tempOutput == null) {
//                    System.out.println("There is no Transaction(" + j + ")");
//                    return false;
//                }
//
//                if (input.UTXO.value != tempOutput.value) {
//                    System.out.println("Transaction(" + j + ") value is wrong");
//                    return false;
//                }
//
//                tempUTXOs.remove(input.transactionOutputId);
//            }
//
//            for (TransactionOutput output : currentTransaction.outputs) {
//                tempUTXOs.put(output.id, output);
//            }
//
//            if (currentTransaction.outputs.get(0).receiver != currentTransaction.receiver) {
//                System.out.println("Transaction(" + j + ") output don't go to receiver");
//                return false;
//            }
//            if (currentTransaction.outputs.get(1).receiver != currentTransaction.sender) {
//                System.out.println("Transaction(" + j + ") output 'change' don't go to sender.");
//                return false;
//            }
//
//        }
//
        }
        System.out.println("Blockchain is valid");
        return true;
    }
}
