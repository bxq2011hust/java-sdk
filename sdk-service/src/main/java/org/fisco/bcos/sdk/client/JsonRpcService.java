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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.fisco.bcos.sdk.channel.ResponseCallback;
import org.fisco.bcos.sdk.client.exceptions.ClientException;
import org.fisco.bcos.sdk.client.protocol.request.JsonRpcRequest;
import org.fisco.bcos.sdk.model.JsonRpcResponse;
import org.fisco.bcos.sdk.model.Message;
import org.fisco.bcos.sdk.model.MsgType;
import org.fisco.bcos.sdk.model.Response;
import org.fisco.bcos.sdk.model.callback.TransactionCallback;
import org.fisco.bcos.sdk.network.Connection;
import org.fisco.bcos.sdk.utils.ChannelUtils;
import org.fisco.bcos.sdk.utils.ObjectMapperFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JsonRpcService {
  protected final ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
  private static Logger logger = LoggerFactory.getLogger(JsonRpcService.class);

  private final Connection connection;
  private final Integer groupId;

  public JsonRpcService(Connection connection, Integer groupId) {
    this.connection = connection;
    this.groupId = groupId;
  }

  public Connection getconnection() {
    return this.connection;
  }

  public <T extends JsonRpcResponse> T sendRequestToGroup(
      JsonRpcRequest request, Class<T> responseType) {
    return this.sendRequestToGroup(request, MsgType.CHANNEL_RPC_REQUEST, responseType);
  }

  public <T extends JsonRpcResponse> T sendRequestToGroup(
      JsonRpcRequest request, MsgType messageType, Class<T> responseType) {
    String response = null;
    try {
      response = this.connection.callMethod(objectMapper.writeValueAsString(request));
    } catch (IOException e) {
      e.printStackTrace();
    }

    if (response == null) {
      throw new ClientException(
          "sendRequestToGroup to "
              + this.groupId
              + " failed for select peers to send message failed, please make sure that the group exists");
    }
    return this.parseResponseIntoJsonRpcResponse(request, response, responseType);
  }

  public <T extends JsonRpcResponse> void asyncSendRequestToGroup(
      JsonRpcRequest request, Class<T> responseType, RespCallback<T> callback) {
    asyncSendRequestToGroup(request, MsgType.CHANNEL_RPC_REQUEST, responseType, callback);
  }

  public <T extends JsonRpcResponse> void asyncSendRequestToGroup(
      JsonRpcRequest request,
      MsgType messageType,
      Class<T> responseType,
      RespCallback<T> callback) {
    Message message = encodeRequestToMessage(request, Short.valueOf((short) messageType.getType()));
    try {
      this.connection.asyncCallMethod(
          message,
          new ResponseCallback() {
            @Override
            public void onResponse(Response response) {
              try {
                // decode the transaction
                T jsonRpcResponse =
                    parseResponseIntoJsonRpcResponse(request, response.getContent(), responseType);
                callback.onResponse(jsonRpcResponse);
              } catch (ClientException e) {
                callback.onError(response);
              }
            }
          });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public <T extends JsonRpcResponse> void asyncSendTransactionToGroup(
      JsonRpcRequest request, TransactionCallback callback, Class<T> responseType) {
    Message message =
        encodeRequestToMessage(
            request, Short.valueOf((short) MsgType.CHANNEL_RPC_REQUEST.getType()));
    try {
      this.connection.asyncCallMethod(
          message,
          new ResponseCallback() {
            @Override
            public void onResponse(Response response) {
              try {
                // decode the transaction
                parseResponseIntoJsonRpcResponse(request, response.getContent(), responseType);
                // FIXME: call callback
              } catch (ClientException e) {
                // fake the transactionReceipt
                callback.onError(e.getErrorCode(), e.getErrorMessage());
              }
            }
          });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  protected <T extends JsonRpcResponse> T parseResponseIntoJsonRpcResponse(
      JsonRpcRequest request, String response, Class<T> responseType) {
    try {
        // parse the response into JsonRPCResponse
        T jsonRpcResponse = objectMapper.readValue(response, responseType);
        if (jsonRpcResponse.getError() != null) {
          logger.error(
              "parseResponseIntoJsonRpcResponse failed for non-empty error message, method: {}, group: {},  retErrorMessage: {}, retErrorCode: {}",
              request.getMethod(),
              this.groupId,
              jsonRpcResponse.getError().getMessage(),
              jsonRpcResponse.getError().getCode());
          throw new ClientException(
              jsonRpcResponse.getError().getCode(),
              jsonRpcResponse.getError().getMessage(),
              "parseResponseIntoJsonRpcResponse failed for non-empty error message, method: "
                  + request.getMethod()
                  + " ,group: "
                  + this.groupId
                  + ",retErrorMessage: "
                  + jsonRpcResponse.getError().getMessage());
        }
        return jsonRpcResponse;


    } catch (JsonProcessingException e) {
      logger.error(
          "parseResponseIntoJsonRpcResponse failed for decode the message exception, errorMessage: {}, groupId: {}",
          e.getMessage(),
          this.groupId);
      throw new ClientException(
          "parseResponseIntoJsonRpcResponse failed for decode the message exceptioned, error message:"
              + e.getMessage(),
          e);
    }
  }

  /**
   * encode the request into message
   *
   * @return the messaged encoded from the request
   */
  private Message encodeRequestToMessage(JsonRpcRequest request, Short messageType) {
    try {
      byte[] encodedData = objectMapper.writeValueAsBytes(request);
      Message message = new Message();
      message.setSeq(ChannelUtils.newSeq());
      message.setResult(0);
      message.setType(messageType);
      message.setData(encodedData);
      logger.trace(
          "encodeRequestToMessage, seq: {}, method: {}, messageType: {}",
          message.getSeq(),
          request.getMethod(),
          message.getType());
      return message;
    } catch (JsonProcessingException e) {
      logger.error(
          "sendRequestToGroup failed for decode the message exceptioned, errorMessge: {}",
          e.getMessage());
      throw new ClientException(
          "sendRequestToGroup to "
              + this.groupId
              + "failed for decode the message exceptioned, error message:"
              + e.getMessage(),
          e);
    }
  }
}
