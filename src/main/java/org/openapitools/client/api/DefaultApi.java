package org.openapitools.client.api;

import org.openapitools.client.ApiException;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiResponse;
import org.openapitools.client.Configuration;
import org.openapitools.client.Pair;

import javax.ws.rs.core.GenericType;

import org.openapitools.client.model.Courier;
import org.openapitools.client.model.Error;
import org.openapitools.client.model.Order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2025-04-14T20:56:32.841989222+03:00[Europe/Moscow]", comments = "Generator version: 7.12.0")
public class DefaultApi {
  private ApiClient apiClient;

  public DefaultApi() {
    this(Configuration.getDefaultApiClient());
  }

  public DefaultApi(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  /**
   * Get the API client
   *
   * @return API client
   */
  public ApiClient getApiClient() {
    return apiClient;
  }

  /**
   * Set the API client
   *
   * @param apiClient an instance of API client
   */
  public void setApiClient(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  /**
   * Создать заказ
   * Позволяет создать заказ с целью тестирования
   * @throws ApiException if fails to make API call
   * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
       <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
       <tr><td> 201 </td><td> Успешный ответ </td><td>  -  </td></tr>
       <tr><td> 0 </td><td> Ошибка </td><td>  -  </td></tr>
     </table>
   */
  public void createOrder() throws ApiException {
    createOrderWithHttpInfo();
  }

  /**
   * Создать заказ
   * Позволяет создать заказ с целью тестирования
   * @return ApiResponse&lt;Void&gt;
   * @throws ApiException if fails to make API call
   * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
       <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
       <tr><td> 201 </td><td> Успешный ответ </td><td>  -  </td></tr>
       <tr><td> 0 </td><td> Ошибка </td><td>  -  </td></tr>
     </table>
   */
  public ApiResponse<Void> createOrderWithHttpInfo() throws ApiException {
    String localVarAccept = apiClient.selectHeaderAccept("application/json");
    String localVarContentType = apiClient.selectHeaderContentType();
    return apiClient.invokeAPI("DefaultApi.createOrder", "/api/v1/orders", "POST", new ArrayList<>(), null,
                               new LinkedHashMap<>(), new LinkedHashMap<>(), new LinkedHashMap<>(), localVarAccept, localVarContentType,
                               null, null, false);
  }
  /**
   * Получить всех курьеров
   * Позволяет получить всех курьеров
   * @return List&lt;Courier&gt;
   * @throws ApiException if fails to make API call
   * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
       <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
       <tr><td> 200 </td><td> Успешный ответ </td><td>  -  </td></tr>
       <tr><td> 0 </td><td> Ошибка </td><td>  -  </td></tr>
     </table>
   */
  public List<Courier> getCouriers() throws ApiException {
    return getCouriersWithHttpInfo().getData();
  }

  /**
   * Получить всех курьеров
   * Позволяет получить всех курьеров
   * @return ApiResponse&lt;List&lt;Courier&gt;&gt;
   * @throws ApiException if fails to make API call
   * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
       <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
       <tr><td> 200 </td><td> Успешный ответ </td><td>  -  </td></tr>
       <tr><td> 0 </td><td> Ошибка </td><td>  -  </td></tr>
     </table>
   */
  public ApiResponse<List<Courier>> getCouriersWithHttpInfo() throws ApiException {
    String localVarAccept = apiClient.selectHeaderAccept("application/json");
    String localVarContentType = apiClient.selectHeaderContentType();
    GenericType<List<Courier>> localVarReturnType = new GenericType<List<Courier>>() {};
    return apiClient.invokeAPI("DefaultApi.getCouriers", "/api/v1/couriers", "GET", new ArrayList<>(), null,
                               new LinkedHashMap<>(), new LinkedHashMap<>(), new LinkedHashMap<>(), localVarAccept, localVarContentType,
                               null, localVarReturnType, false);
  }
  /**
   * Получить все незавершенные заказы
   * Позволяет получить все незавершенные
   * @return List&lt;Order&gt;
   * @throws ApiException if fails to make API call
   * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
       <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
       <tr><td> 200 </td><td> Успешный ответ </td><td>  -  </td></tr>
       <tr><td> 0 </td><td> Ошибка </td><td>  -  </td></tr>
     </table>
   */
  public List<Order> getOrders() throws ApiException {
    return getOrdersWithHttpInfo().getData();
  }

  /**
   * Получить все незавершенные заказы
   * Позволяет получить все незавершенные
   * @return ApiResponse&lt;List&lt;Order&gt;&gt;
   * @throws ApiException if fails to make API call
   * @http.response.details
     <table border="1">
       <caption>Response Details</caption>
       <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
       <tr><td> 200 </td><td> Успешный ответ </td><td>  -  </td></tr>
       <tr><td> 0 </td><td> Ошибка </td><td>  -  </td></tr>
     </table>
   */
  public ApiResponse<List<Order>> getOrdersWithHttpInfo() throws ApiException {
    String localVarAccept = apiClient.selectHeaderAccept("application/json");
    String localVarContentType = apiClient.selectHeaderContentType();
    GenericType<List<Order>> localVarReturnType = new GenericType<List<Order>>() {};
    return apiClient.invokeAPI("DefaultApi.getOrders", "/api/v1/orders/active", "GET", new ArrayList<>(), null,
                               new LinkedHashMap<>(), new LinkedHashMap<>(), new LinkedHashMap<>(), localVarAccept, localVarContentType,
                               null, localVarReturnType, false);
  }
}
