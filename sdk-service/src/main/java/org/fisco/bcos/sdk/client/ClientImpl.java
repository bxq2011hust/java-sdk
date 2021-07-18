/**
 * Copyright 2014-2020 [fisco-dev]
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fisco.bcos.sdk.client;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.concurrent.Semaphore;
import org.fisco.bcos.sdk.client.protocol.request.JsonRpcMethods;
import org.fisco.bcos.sdk.client.protocol.request.JsonRpcRequest;
import org.fisco.bcos.sdk.client.protocol.request.Transaction;
import org.fisco.bcos.sdk.client.protocol.response.*;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.model.callback.TransactionCallback;
import org.fisco.bcos.sdk.network.Connection;
import org.fisco.bcos.sdk.utils.Numeric;

public class ClientImpl implements Client {
    private final JsonRpcService jsonRpcService;
    private final Integer groupId;
    private final Integer DefaultGroupId = Integer.valueOf(1);
    private final CryptoSuite cryptoSuite;
    private final NodeInfo nodeInfo;
    private final Connection connection;

    protected ClientImpl(Connection connection, Integer groupId, CryptoSuite cryptoSuite) {
        this.jsonRpcService = new JsonRpcService(connection, groupId);
        this.groupId = groupId;
        this.cryptoSuite = cryptoSuite;
        this.nodeInfo =
                this.jsonRpcService.sendRequestToGroup(
                        new JsonRpcRequest(JsonRpcMethods.GET_NODE_INFO, Arrays.asList()),
                        NodeInfo.class);
        // FIXME: get node info by call getNodeInfo
        //        nodeVersion;
        this.connection = connection;
        // send request to the group, and get the blockNumber information
        getBlockLimit();
    }

    protected ClientImpl(Connection connection) {
        this.jsonRpcService = new JsonRpcService(connection, null);
        this.groupId = null;
        this.cryptoSuite = null;
        this.nodeInfo = null;
        this.connection = null;
    }

    @Override
    public Connection getConnection() {
        return this.connection;
    }

    @Override
    public CryptoSuite getCryptoSuite() {
        return this.cryptoSuite;
    }

    @Override
    public NodeInfo.NodeInformation getNodeInfo() {
        return this.nodeInfo.getNodeInfo();
    }

    @Override
    public Integer getCryptoType() {
        return this.cryptoSuite.getCryptoTypeConfig();
    }

    @Override
    public Integer getGroupId() {
        return this.groupId;
    }

    @Override
    public SendTransaction sendRawTransaction(String signedTransactionData) {
        return this.jsonRpcService.sendRequestToGroup(
                new JsonRpcRequest(
                        JsonRpcMethods.SEND_RAWTRANSACTION,
                        Arrays.asList(this.groupId, signedTransactionData)),
                SendTransaction.class);
    }

    @Override
    public void sendRawTransactionAsync(
            String signedTransactionData, RespCallback<SendTransaction> callback) {
        this.jsonRpcService.asyncSendRequestToGroup(
                new JsonRpcRequest(
                        JsonRpcMethods.SEND_RAWTRANSACTION,
                        Arrays.asList(this.groupId, signedTransactionData)),
                SendTransaction.class,
                callback);
    }

    @Override
    public Call call(Transaction transaction) {
        return this.jsonRpcService.sendRequestToGroup(
                new JsonRpcRequest(JsonRpcMethods.CALL, Arrays.asList(this.groupId, transaction)),
                Call.class);
    }

    @Override
    public void callAsync(Transaction transaction, RespCallback<Call> callback) {
        this.jsonRpcService.asyncSendRequestToGroup(
                new JsonRpcRequest(JsonRpcMethods.CALL, Arrays.asList(this.groupId, transaction)),
                Call.class,
                callback);
    }

    @Override
    public SendTransaction sendRawTransactionAndGetProof(String signedTransactionData) {
        return this.jsonRpcService.sendRequestToGroup(
                new JsonRpcRequest(
                        JsonRpcMethods.SEND_RAWTRANSACTION_AND_GET_PROOF,
                        Arrays.asList(this.groupId, signedTransactionData)),
                SendTransaction.class);
    }

    @Override
    public void sendRawTransactionAndGetProofAsync(
            String signedTransactionData, RespCallback<SendTransaction> callback) {
        this.jsonRpcService.asyncSendRequestToGroup(
                new JsonRpcRequest(
                        JsonRpcMethods.SEND_RAWTRANSACTION_AND_GET_PROOF,
                        Arrays.asList(this.groupId, signedTransactionData)),
                SendTransaction.class,
                callback);
    }

    @Override
    public BlockNumber getBlockNumber() {
        // create request
        JsonRpcRequest request =
                new JsonRpcRequest(JsonRpcMethods.GET_BLOCK_NUMBER, Arrays.asList(this.groupId));
        return this.jsonRpcService.sendRequestToGroup(request, BlockNumber.class);
    }

    @Override
    public void getBlockNumberAsync(RespCallback<BlockNumber> callback) {
        this.jsonRpcService.asyncSendRequestToGroup(
                new JsonRpcRequest(JsonRpcMethods.GET_BLOCK_NUMBER, Arrays.asList(this.groupId)),
                BlockNumber.class,
                callback);
    }

    @Override
    public Code getCode(String address) {
        // create request
        JsonRpcRequest request =
                new JsonRpcRequest(JsonRpcMethods.GET_CODE, Arrays.asList(this.groupId, address));
        return this.jsonRpcService.sendRequestToGroup(request, Code.class);
    }

    @Override
    public void getCodeAsync(String address, RespCallback<Code> callback) {
        this.jsonRpcService.asyncSendRequestToGroup(
                new JsonRpcRequest(JsonRpcMethods.GET_CODE, Arrays.asList(this.groupId, address)),
                Code.class,
                callback);
    }

    @Override
    public TotalTransactionCount getTotalTransactionCount() {
        // create request for getTotalTransactionCount
        JsonRpcRequest request =
                new JsonRpcRequest(
                        JsonRpcMethods.GET_TOTAL_TRANSACTION_COUNT, Arrays.asList(this.groupId));
        return this.jsonRpcService.sendRequestToGroup(request, TotalTransactionCount.class);
    }

    @Override
    public void getTotalTransactionCountAsync(RespCallback<TotalTransactionCount> callback) {
        this.jsonRpcService.asyncSendRequestToGroup(
                new JsonRpcRequest(
                        JsonRpcMethods.GET_TOTAL_TRANSACTION_COUNT, Arrays.asList(this.groupId)),
                TotalTransactionCount.class,
                callback);
    }

    @Override
    public BcosBlock getBlockByHash(String blockHash, boolean returnFullTransactionObjects) {
        return this.jsonRpcService.sendRequestToGroup(
                new JsonRpcRequest(
                        JsonRpcMethods.GET_BLOCK_BY_HASH,
                        Arrays.asList(this.groupId, blockHash, returnFullTransactionObjects)),
                BcosBlock.class);
    }

    @Override
    public void getBlockByHashAsync(
            String blockHash,
            boolean returnFullTransactionObjects,
            RespCallback<BcosBlock> callback) {
        this.jsonRpcService.asyncSendRequestToGroup(
                new JsonRpcRequest(
                        JsonRpcMethods.GET_BLOCK_BY_HASH,
                        Arrays.asList(this.groupId, blockHash, returnFullTransactionObjects)),
                BcosBlock.class,
                callback);
    }

    @Override
    public BcosBlock getBlockByNumber(
            BigInteger blockNumber, boolean returnFullTransactionObjects) {
        return this.jsonRpcService.sendRequestToGroup(
                new JsonRpcRequest(
                        JsonRpcMethods.GET_BLOCK_BY_NUMBER,
                        Arrays.asList(
                                this.groupId,
                                String.valueOf(blockNumber),
                                returnFullTransactionObjects)),
                BcosBlock.class);
    }

    @Override
    public void getBlockByNumberAsync(
            BigInteger blockNumber,
            boolean returnFullTransactionObjects,
            RespCallback<BcosBlock> callback) {
        this.jsonRpcService.asyncSendRequestToGroup(
                new JsonRpcRequest(
                        JsonRpcMethods.GET_BLOCK_BY_NUMBER,
                        Arrays.asList(
                                this.groupId,
                                String.valueOf(blockNumber),
                                returnFullTransactionObjects)),
                BcosBlock.class,
                callback);
    }

    @Override
    public BlockHash getBlockHashByNumber(BigInteger blockNumber) {
        return this.jsonRpcService.sendRequestToGroup(
                new JsonRpcRequest(
                        JsonRpcMethods.GET_BLOCKHASH_BY_NUMBER,
                        Arrays.asList(this.groupId, String.valueOf(blockNumber))),
                BlockHash.class);
    }

    @Override
    public void getBlockHashByNumberAsync(
            BigInteger blockNumber, RespCallback<BlockHash> callback) {
        this.jsonRpcService.asyncSendRequestToGroup(
                new JsonRpcRequest(
                        JsonRpcMethods.GET_BLOCKHASH_BY_NUMBER,
                        Arrays.asList(this.groupId, String.valueOf(blockNumber))),
                BlockHash.class,
                callback);
    }

    @Override
    public BcosBlockHeader getBlockHeaderByHash(String blockHash, boolean returnSignatureList) {
        return this.jsonRpcService.sendRequestToGroup(
                new JsonRpcRequest(
                        JsonRpcMethods.GET_BLOCKHEADER_BY_HASH,
                        Arrays.asList(this.groupId, blockHash, returnSignatureList)),
                BcosBlockHeader.class);
    }

    @Override
    public void getBlockHeaderByHashAsync(
            String blockHash, boolean returnSignatureList, RespCallback<BcosBlockHeader> callback) {
        this.jsonRpcService.asyncSendRequestToGroup(
                new JsonRpcRequest(
                        JsonRpcMethods.GET_BLOCKHEADER_BY_HASH,
                        Arrays.asList(this.groupId, blockHash, returnSignatureList)),
                BcosBlockHeader.class,
                callback);
    }

    @Override
    public BcosBlockHeader getBlockHeaderByNumber(
            BigInteger blockNumber, boolean returnSignatureList) {
        return this.jsonRpcService.sendRequestToGroup(
                new JsonRpcRequest(
                        JsonRpcMethods.GET_BLOCKHEADER_BY_NUMBER,
                        Arrays.asList(
                                this.groupId, String.valueOf(blockNumber), returnSignatureList)),
                BcosBlockHeader.class);
    }

    @Override
    public void getBlockHeaderByNumberAsync(
            BigInteger blockNumber,
            boolean returnSignatureList,
            RespCallback<BcosBlockHeader> callback) {
        this.jsonRpcService.asyncSendRequestToGroup(
                new JsonRpcRequest(
                        JsonRpcMethods.GET_BLOCKHEADER_BY_NUMBER,
                        Arrays.asList(
                                this.groupId, String.valueOf(blockNumber), returnSignatureList)),
                BcosBlockHeader.class,
                callback);
    }

    @Override
    public BcosTransaction getTransactionByHash(String transactionHash) {
        return this.jsonRpcService.sendRequestToGroup(
                new JsonRpcRequest(
                        JsonRpcMethods.GET_TRANSACTION_BY_HASH,
                        Arrays.asList(this.groupId, transactionHash)),
                BcosTransaction.class);
    }

    @Override
    public void getTransactionByHashAsync(
            String transactionHash, RespCallback<BcosTransaction> callback) {
        this.jsonRpcService.asyncSendRequestToGroup(
                new JsonRpcRequest(
                        JsonRpcMethods.GET_TRANSACTION_BY_HASH,
                        Arrays.asList(this.groupId, transactionHash)),
                BcosTransaction.class,
                callback);
    }

    @Override
    public TransactionWithProof getTransactionByHashWithProof(String transactionHash) {
        return this.jsonRpcService.sendRequestToGroup(
                new JsonRpcRequest(
                        JsonRpcMethods.GET_TRANSACTION_BY_HASH_WITH_PROOF,
                        Arrays.asList(this.groupId, transactionHash)),
                TransactionWithProof.class);
    }

    @Override
    public void getTransactionByHashWithProofAsync(
            String transactionHash, RespCallback<TransactionWithProof> callback) {
        this.jsonRpcService.asyncSendRequestToGroup(
                new JsonRpcRequest(
                        JsonRpcMethods.GET_TRANSACTION_BY_HASH_WITH_PROOF,
                        Arrays.asList(this.groupId, transactionHash)),
                TransactionWithProof.class,
                callback);
    }

    @Override
    public BcosTransaction getTransactionByBlockNumberAndIndex(
            BigInteger blockNumber, BigInteger transactionIndex) {
        return this.jsonRpcService.sendRequestToGroup(
                new JsonRpcRequest(
                        JsonRpcMethods.GET_TRANSACTION_BY_BLOCKNUMBER_AND_INDEX,
                        Arrays.asList(
                                this.groupId,
                                String.valueOf(blockNumber),
                                Numeric.encodeQuantity(transactionIndex))),
                BcosTransaction.class);
    }

    @Override
    public void getTransactionByBlockNumberAndIndexAsync(
            BigInteger blockNumber,
            BigInteger transactionIndex,
            RespCallback<BcosTransaction> callback) {
        this.jsonRpcService.asyncSendRequestToGroup(
                new JsonRpcRequest(
                        JsonRpcMethods.GET_TRANSACTION_BY_BLOCKNUMBER_AND_INDEX,
                        Arrays.asList(
                                this.groupId,
                                String.valueOf(blockNumber),
                                Numeric.encodeQuantity(transactionIndex))),
                BcosTransaction.class,
                callback);
    }

    @Override
    public BcosTransaction getTransactionByBlockHashAndIndex(
            String blockHash, BigInteger transactionIndex) {
        return jsonRpcService.sendRequestToGroup(
                new JsonRpcRequest(
                        JsonRpcMethods.GET_TRANSACTION_BY_BLOCKHASH_AND_INDEX,
                        Arrays.asList(
                                this.groupId, blockHash, Numeric.encodeQuantity(transactionIndex))),
                BcosTransaction.class);
    }

    @Override
    public void getTransactionByBlockHashAndIndexAsync(
            String blockHash, BigInteger transactionIndex, RespCallback<BcosTransaction> callback) {
        jsonRpcService.asyncSendRequestToGroup(
                new JsonRpcRequest(
                        JsonRpcMethods.GET_TRANSACTION_BY_BLOCKHASH_AND_INDEX,
                        Arrays.asList(
                                this.groupId, blockHash, Numeric.encodeQuantity(transactionIndex))),
                BcosTransaction.class,
                callback);
    }

    @Override
    public BcosTransactionReceipt getTransactionReceipt(String transactionHash) {
        return this.jsonRpcService.sendRequestToGroup(
                new JsonRpcRequest(
                        JsonRpcMethods.GET_TRANSACTIONRECEIPT,
                        Arrays.asList(this.groupId, transactionHash)),
                BcosTransactionReceipt.class);
    }

    @Override
    public void getTransactionReceiptAsync(
            String transactionHash, RespCallback<BcosTransactionReceipt> callback) {
        this.jsonRpcService.asyncSendRequestToGroup(
                new JsonRpcRequest(
                        JsonRpcMethods.GET_TRANSACTIONRECEIPT,
                        Arrays.asList(this.groupId, transactionHash)),
                BcosTransactionReceipt.class,
                callback);
    }

    @Override
    public TransactionReceiptWithProof getTransactionReceiptByHashWithProof(
            String transactionHash) {
        return this.jsonRpcService.sendRequestToGroup(
                new JsonRpcRequest(
                        JsonRpcMethods.GET_TRANSACTION_RECEIPT_BY_HASH_WITH_PROOF,
                        Arrays.asList(this.groupId, transactionHash)),
                TransactionReceiptWithProof.class);
    }

    @Override
    public void getTransactionReceiptByHashWithProofAsync(
            String transactionHash, RespCallback<TransactionReceiptWithProof> callback) {
        this.jsonRpcService.asyncSendRequestToGroup(
                new JsonRpcRequest(
                        JsonRpcMethods.GET_TRANSACTION_RECEIPT_BY_HASH_WITH_PROOF,
                        Arrays.asList(this.groupId, transactionHash)),
                TransactionReceiptWithProof.class,
                callback);
    }

    @Override
    public PendingTransactions getPendingTransaction() {
        return this.jsonRpcService.sendRequestToGroup(
                new JsonRpcRequest(
                        JsonRpcMethods.GET_PENDING_TRANSACTIONS, Arrays.asList(this.groupId)),
                PendingTransactions.class);
    }

    @Override
    public void getPendingTransactionAsync(RespCallback<PendingTransactions> callback) {
        this.jsonRpcService.asyncSendRequestToGroup(
                new JsonRpcRequest(
                        JsonRpcMethods.GET_PENDING_TRANSACTIONS, Arrays.asList(this.groupId)),
                PendingTransactions.class,
                callback);
    }

    @Override
    public PendingTxSize getPendingTxSize() {
        return this.jsonRpcService.sendRequestToGroup(
                new JsonRpcRequest(JsonRpcMethods.GET_PENDING_TX_SIZE, Arrays.asList(this.groupId)),
                PendingTxSize.class);
    }

    @Override
    public void getPendingTxSizeAsync(RespCallback<PendingTxSize> callback) {
        this.jsonRpcService.asyncSendRequestToGroup(
                new JsonRpcRequest(JsonRpcMethods.GET_PENDING_TX_SIZE, Arrays.asList(this.groupId)),
                PendingTxSize.class,
                callback);
    }

    @Override
    public BigInteger getBlockLimit() {
        Integer groupId = Integer.valueOf(this.groupId);
        return this.getBlockNumber().getBlockNumber().add(BigInteger.valueOf(500));
    }

    @Override
    public Peers getPeers() {
        return this.jsonRpcService.sendRequestToGroup(
                new JsonRpcRequest(JsonRpcMethods.GET_PEERS, Arrays.asList(DefaultGroupId)),
                Peers.class);
    }

    @Override
    public void getPeersAsync(RespCallback<Peers> callback) {
        this.jsonRpcService.asyncSendRequestToGroup(
                new JsonRpcRequest(JsonRpcMethods.GET_PEERS, Arrays.asList(this.groupId)),
                Peers.class,
                callback);
    }

    @Override
    public ObserverList getObserverList() {
        return this.jsonRpcService.sendRequestToGroup(
                new JsonRpcRequest(JsonRpcMethods.GET_OBSERVER_LIST, Arrays.asList(this.groupId)),
                ObserverList.class);
    }

    @Override
    public void getObserverList(RespCallback<ObserverList> callback) {
        this.jsonRpcService.asyncSendRequestToGroup(
                new JsonRpcRequest(JsonRpcMethods.GET_OBSERVER_LIST, Arrays.asList(this.groupId)),
                ObserverList.class,
                callback);
    }

    @Override
    public SealerList getSealerList() {
        return this.jsonRpcService.sendRequestToGroup(
                new JsonRpcRequest(JsonRpcMethods.GET_SEALER_LIST, Arrays.asList(this.groupId)),
                SealerList.class);
    }

    @Override
    public void getSealerListAsync(RespCallback<SealerList> callback) {
        this.jsonRpcService.asyncSendRequestToGroup(
                new JsonRpcRequest(JsonRpcMethods.GET_SEALER_LIST, Arrays.asList(this.groupId)),
                SealerList.class,
                callback);
    }

    @Override
    public PbftView getPbftView() {
        return this.jsonRpcService.sendRequestToGroup(
                new JsonRpcRequest(JsonRpcMethods.GET_PBFT_VIEW, Arrays.asList(this.groupId)),
                PbftView.class);
    }

    @Override
    public void getPbftViewAsync(RespCallback<PbftView> callback) {
        this.jsonRpcService.asyncSendRequestToGroup(
                new JsonRpcRequest(JsonRpcMethods.GET_PBFT_VIEW, Arrays.asList(this.groupId)),
                PbftView.class,
                callback);
    }

    @Override
    public ConsensusStatus getConsensusStatus() {
        return this.jsonRpcService.sendRequestToGroup(
                new JsonRpcRequest(
                        JsonRpcMethods.GET_CONSENSUS_STATUS, Arrays.asList(this.groupId)),
                ConsensusStatus.class);
    }

    @Override
    public void getConsensusStates(RespCallback<ConsensusStatus> callback) {
        this.jsonRpcService.asyncSendRequestToGroup(
                new JsonRpcRequest(
                        JsonRpcMethods.GET_CONSENSUS_STATUS, Arrays.asList(this.groupId)),
                ConsensusStatus.class,
                callback);
    }

    @Override
    public SystemConfig getSystemConfigByKey(String key) {
        return this.jsonRpcService.sendRequestToGroup(
                new JsonRpcRequest(
                        JsonRpcMethods.GET_SYSTEM_CONFIG_BY_KEY, Arrays.asList(this.groupId, key)),
                SystemConfig.class);
    }

    @Override
    public void getSystemConfigByKeyAsync(String key, RespCallback<SystemConfig> callback) {
        this.jsonRpcService.asyncSendRequestToGroup(
                new JsonRpcRequest(
                        JsonRpcMethods.GET_SYSTEM_CONFIG_BY_KEY, Arrays.asList(this.groupId)),
                SystemConfig.class,
                callback);
    }

    @Override
    public SyncStatus getSyncStatus() {
        return this.jsonRpcService.sendRequestToGroup(
                new JsonRpcRequest(JsonRpcMethods.GET_SYNC_STATUS, Arrays.asList(this.groupId)),
                SyncStatus.class);
    }

    @Override
    public void getSyncStatus(RespCallback<SyncStatus> callback) {
        this.jsonRpcService.asyncSendRequestToGroup(
                new JsonRpcRequest(JsonRpcMethods.GET_SYNC_STATUS, Arrays.asList(this.groupId)),
                SyncStatus.class,
                callback);
    }

    class SynchronousTransactionCallback extends TransactionCallback {
        public TransactionReceipt receipt;
        public Semaphore semaphore = new Semaphore(1, true);

        SynchronousTransactionCallback() {
            try {
                semaphore.acquire(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        @Override
        public void onTimeout() {
            super.onTimeout();
            semaphore.release();
        }

        // wait until get the transactionReceipt
        @Override
        public void onResponse(TransactionReceipt receipt) {
            this.receipt = receipt;
            semaphore.release();
        }
    }

    @Override
    public TransactionReceipt sendRawTransactionAndGetReceipt(String signedTransactionData) {
        SynchronousTransactionCallback callback = new SynchronousTransactionCallback();
        sendRawTransactionAndGetReceiptAsync(signedTransactionData, callback);
        try {
            callback.semaphore.acquire(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return callback.receipt;
    }

    @Override
    public void sendRawTransactionAndGetReceiptAsync(
            String signedTransactionData, TransactionCallback callback) {
        this.jsonRpcService.asyncSendTransactionToGroup(
                new JsonRpcRequest(
                        JsonRpcMethods.SEND_RAWTRANSACTION,
                        Arrays.asList(this.groupId, signedTransactionData)),
                callback,
                SendTransaction.class);
    }

    @Override
    public void sendRawTransactionAndGetReceiptWithProofAsync(
            String signedTransactionData, TransactionCallback callback) {
        this.jsonRpcService.asyncSendTransactionToGroup(
                new JsonRpcRequest(
                        JsonRpcMethods.SEND_RAWTRANSACTION_AND_GET_PROOF,
                        Arrays.asList(this.groupId, signedTransactionData)),
                callback,
                SendTransaction.class);
    }

    @Override
    public TransactionReceipt sendRawTransactionAndGetReceiptWithProof(
            String signedTransactionData) {
        SynchronousTransactionCallback callback = new SynchronousTransactionCallback();
        sendRawTransactionAndGetReceiptWithProofAsync(signedTransactionData, callback);
        try {
            callback.semaphore.acquire(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return callback.receipt;
    }

    @Override
    public BcosTransactionReceiptsDecoder getBatchReceiptsByBlockNumberAndRange(
            BigInteger blockNumber, String from, String count) {
        return this.jsonRpcService.sendRequestToGroup(
                new JsonRpcRequest(
                        JsonRpcMethods.GET_BATCH_RECEIPT_BY_BLOCK_NUMBER_AND_RANGE,
                        Arrays.asList(
                                this.groupId, String.valueOf(blockNumber), from, count, true)),
                BcosTransactionReceiptsDecoder.class);
    }

    @Override
    public BcosTransactionReceiptsDecoder getBatchReceiptsByBlockHashAndRange(
            String blockHash, String from, String count) {
        return this.jsonRpcService.sendRequestToGroup(
                new JsonRpcRequest(
                        JsonRpcMethods.GET_BATCH_RECEIPT_BY_BLOCK_HASH_AND_RANGE,
                        Arrays.asList(this.groupId, blockHash, from, count, true)),
                BcosTransactionReceiptsDecoder.class);
    }

    @Override
    public void stop() {
        Thread.currentThread().interrupt();
    }
}
