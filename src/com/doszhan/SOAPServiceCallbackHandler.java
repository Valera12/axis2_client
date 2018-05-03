/**
 * SOAPServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.7.7  Built on : Nov 20, 2017 (11:41:20 GMT)
 */
package com.doszhan;


/**
 *  SOAPServiceCallbackHandler Callback class, Users can extend this class and implement
 *  their own receiveResult and receiveError methods.
 */
public abstract class SOAPServiceCallbackHandler {
    protected Object clientData;

    /**
     * User can pass in any object that needs to be accessed once the NonBlocking
     * Web service call is finished and appropriate method of this CallBack is called.
     * @param clientData Object mechanism by which the user can pass in user data
     * that will be avilable at the time this callback is called.
     */
    public SOAPServiceCallbackHandler(Object clientData) {
        this.clientData = clientData;
    }

    /**
     * Please use this constructor if you don't want to set any clientData
     */
    public SOAPServiceCallbackHandler() {
        this.clientData = null;
    }

    /**
     * Get the client data
     */
    public Object getClientData() {
        return clientData;
    }

    /**
     * auto generated Axis2 call back method for fetchArticleList method
     * override this method for handling normal response from fetchArticleList operation
     */
    public void receiveResultfetchArticleList(
        com.doszhan.SOAPServiceStub.FetchArticleListResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from fetchArticleList operation
     */
    public void receiveErrorfetchArticleList(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for delArticle method
     * override this method for handling normal response from delArticle operation
     */
    public void receiveResultdelArticle(
        com.doszhan.SOAPServiceStub.DelArticleResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from delArticle operation
     */
    public void receiveErrordelArticle(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for addArticle method
     * override this method for handling normal response from addArticle operation
     */
    public void receiveResultaddArticle(
        com.doszhan.SOAPServiceStub.AddArticleResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from addArticle operation
     */
    public void receiveErroraddArticle(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for updArticle method
     * override this method for handling normal response from updArticle operation
     */
    public void receiveResultupdArticle(
        com.doszhan.SOAPServiceStub.UpdArticleResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from updArticle operation
     */
    public void receiveErrorupdArticle(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getArticle method
     * override this method for handling normal response from getArticle operation
     */
    public void receiveResultgetArticle(
        com.doszhan.SOAPServiceStub.GetArticleResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getArticle operation
     */
    public void receiveErrorgetArticle(java.lang.Exception e) {
    }
}
