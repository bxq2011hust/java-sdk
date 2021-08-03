// **********************************************************************
// This file was generated by a TARS parser!
// TARS version 1.7.2.
// **********************************************************************

package org.fisco.bcos.sdk.client.protocol.model;

import com.qq.tars.protocol.tars.TarsInputStream;
import com.qq.tars.protocol.tars.TarsOutputStream;
import com.qq.tars.protocol.tars.annotation.TarsStruct;
import com.qq.tars.protocol.tars.annotation.TarsStructProperty;
import com.qq.tars.protocol.util.TarsUtil;
import org.fisco.bcos.sdk.model.TransactionReceipt;

@TarsStruct
public class Block {

    @TarsStructProperty(order = 1, isRequire = false)
    public int version = 0;
    @TarsStructProperty(order = 2, isRequire = false)
    public int type = 0;
    @TarsStructProperty(order = 3, isRequire = false)
    public BlockHeader blockHeader = null;
    @TarsStructProperty(order = 4, isRequire = false)
    public java.util.List<Transaction> transactions = null;
    @TarsStructProperty(order = 5, isRequire = false)
    public java.util.List<TransactionReceipt> receipts = null;
    @TarsStructProperty(order = 6, isRequire = false)
    public java.util.List<byte[]> transactionsHash = null;
    @TarsStructProperty(order = 7, isRequire = false)
    public java.util.List<byte[]> receiptsHash = null;
    @TarsStructProperty(order = 8, isRequire = false)
    public java.util.List<String> nonceList = null;

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public BlockHeader getBlockHeader() {
        return this.blockHeader;
    }

    public void setBlockHeader(BlockHeader blockHeader) {
        this.blockHeader = blockHeader;
    }

    public java.util.List<Transaction> getTransactions() {
        return this.transactions;
    }

    public void setTransactions(java.util.List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public java.util.List<TransactionReceipt> getReceipts() {
        return this.receipts;
    }

    public void setReceipts(java.util.List<TransactionReceipt> receipts) {
        this.receipts = receipts;
    }

    public java.util.List<byte[]> getTransactionsHash() {
        return this.transactionsHash;
    }

    public void setTransactionsHash(java.util.List<byte[]> transactionsHash) {
        this.transactionsHash = transactionsHash;
    }

    public java.util.List<byte[]> getReceiptsHash() {
        return this.receiptsHash;
    }

    public void setReceiptsHash(java.util.List<byte[]> receiptsHash) {
        this.receiptsHash = receiptsHash;
    }

    public java.util.List<String> getNonceList() {
        return this.nonceList;
    }

    public void setNonceList(java.util.List<String> nonceList) {
        this.nonceList = nonceList;
    }

    public Block() {
    }

    public Block(int version, int type, BlockHeader blockHeader, java.util.List<Transaction> transactions, java.util.List<TransactionReceipt> receipts, java.util.List<byte[]> transactionsHash, java.util.List<byte[]> receiptsHash, java.util.List<String> nonceList) {
        this.version = version;
        this.type = type;
        this.blockHeader = blockHeader;
        this.transactions = transactions;
        this.receipts = receipts;
        this.transactionsHash = transactionsHash;
        this.receiptsHash = receiptsHash;
        this.nonceList = nonceList;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + TarsUtil.hashCode(this.version);
        result = prime * result + TarsUtil.hashCode(this.type);
        result = prime * result + TarsUtil.hashCode(this.blockHeader);
        result = prime * result + TarsUtil.hashCode(this.transactions);
        result = prime * result + TarsUtil.hashCode(this.receipts);
        result = prime * result + TarsUtil.hashCode(this.transactionsHash);
        result = prime * result + TarsUtil.hashCode(this.receiptsHash);
        result = prime * result + TarsUtil.hashCode(this.nonceList);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Block)) {
            return false;
        }
        Block other = (Block) obj;
        return (
                TarsUtil.equals(this.version, other.version) &&
                        TarsUtil.equals(this.type, other.type) &&
                        TarsUtil.equals(this.blockHeader, other.blockHeader) &&
                        TarsUtil.equals(this.transactions, other.transactions) &&
                        TarsUtil.equals(this.receipts, other.receipts) &&
                        TarsUtil.equals(this.transactionsHash, other.transactionsHash) &&
                        TarsUtil.equals(this.receiptsHash, other.receiptsHash) &&
                        TarsUtil.equals(this.nonceList, other.nonceList)
        );
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Block(");
        sb.append("version:");
        sb.append(this.version);
        sb.append(", ");
        sb.append("type:");
        sb.append(this.type);
        sb.append(", ");
        sb.append("blockHeader:");
        if (this.blockHeader == null) {
            sb.append("null");
        } else {
            sb.append(this.blockHeader);
        }
        sb.append(", ");
        sb.append("transactions:");
        if (this.transactions == null) {
            sb.append("null");
        } else {
            sb.append(this.transactions);
        }
        sb.append(", ");
        sb.append("receipts:");
        if (this.receipts == null) {
            sb.append("null");
        } else {
            sb.append(this.receipts);
        }
        sb.append(", ");
        sb.append("transactionsHash:");
        if (this.transactionsHash == null) {
            sb.append("null");
        } else {
            sb.append(this.transactionsHash);
        }
        sb.append(", ");
        sb.append("receiptsHash:");
        if (this.receiptsHash == null) {
            sb.append("null");
        } else {
            sb.append(this.receiptsHash);
        }
        sb.append(", ");
        sb.append("nonceList:");
        if (this.nonceList == null) {
            sb.append("null");
        } else {
            sb.append(this.nonceList);
        }
        sb.append(")");
        return sb.toString();
    }

    public void writeTo(TarsOutputStream _os) {
        _os.write(this.version, 1);
        _os.write(this.type, 2);
        if (null != this.blockHeader) {
            _os.write(this.blockHeader, 3);
        }
        if (null != this.transactions) {
            _os.write(this.transactions, 4);
        }
        if (null != this.receipts) {
            _os.write(this.receipts, 5);
        }
        if (null != this.transactionsHash) {
            _os.write(this.transactionsHash, 6);
        }
        if (null != this.receiptsHash) {
            _os.write(this.receiptsHash, 7);
        }
        if (null != this.nonceList) {
            _os.write(this.nonceList, 8);
        }
    }

    static BlockHeader cache_blockHeader;

    static {
        cache_blockHeader = new BlockHeader();
    }

    static java.util.List<Transaction> cache_transactions;

    static {
        cache_transactions = new java.util.ArrayList<Transaction>();
        Transaction var_16 = new Transaction();
        cache_transactions.add(var_16);
    }

    static java.util.List<TransactionReceipt> cache_receipts;

    static {
        cache_receipts = new java.util.ArrayList<TransactionReceipt>();
        TransactionReceipt var_17 = new TransactionReceipt();
        cache_receipts.add(var_17);
    }

    static java.util.List<byte[]> cache_transactionsHash;

    static {
        cache_transactionsHash = new java.util.ArrayList<byte[]>();
        byte[] var_18 = new byte[1];
        byte var_19 = (byte) 0;
        var_18[0] = var_19;
        cache_transactionsHash.add(var_18);
    }

    static java.util.List<byte[]> cache_receiptsHash;

    static {
        cache_receiptsHash = new java.util.ArrayList<byte[]>();
        byte[] var_20 = new byte[1];
        byte var_21 = (byte) 0;
        var_20[0] = var_21;
        cache_receiptsHash.add(var_20);
    }

    static java.util.List<String> cache_nonceList;

    static {
        cache_nonceList = new java.util.ArrayList<String>();
        String var_22 = "";
        cache_nonceList.add(var_22);
    }

    public void readFrom(TarsInputStream _is) {
        this.version = _is.read(this.version, 1, false);
        this.type = _is.read(this.type, 2, false);
        this.blockHeader = (BlockHeader) _is.read(cache_blockHeader, 3, false);
        this.transactions = (java.util.List<Transaction>) _is.read(cache_transactions, 4, false);
        this.receipts = (java.util.List<TransactionReceipt>) _is.read(cache_receipts, 5, false);
        this.transactionsHash = (java.util.List<byte[]>) _is.read(cache_transactionsHash, 6, false);
        this.receiptsHash = (java.util.List<byte[]>) _is.read(cache_receiptsHash, 7, false);
        this.nonceList = (java.util.List<String>) _is.read(cache_nonceList, 8, false);
    }

}