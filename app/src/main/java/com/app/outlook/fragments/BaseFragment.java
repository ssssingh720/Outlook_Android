package com.app.outlook.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.app.outlook.activities.AppBaseActivity;
import com.app.outlook.listener.ServerCallback;
import com.app.outlook.manager.SessionManager;
import com.app.outlook.manager.StateHolder;
import com.app.outlook.modal.FeedParams;
import com.app.outlook.networking.RequestManager;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

/**
 * Created by srajendrakumar on 09/09/15.
 */
public class BaseFragment extends Fragment implements ServerCallback {

    protected View mView;
    protected StateHolder stateHolder;
    protected Context fragmentContext;
    private Toast mToast;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToast = Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);
    }

    /*
    * Method to make the server call
    * @param methodname String value which defines the method to be called in the api
    * @param params Hashmap which contains the params to be passed to the backend
    */
    // Server Calls
    public void placeRequest(String methodName, Class clazz, HashMap<String, String> params,boolean isPOST,String action) {
        RequestManager.getInstance(mView.getContext()).placeRequest(methodName, clazz, this, params, isPOST,action);
    }

    public void placeRequest(String methodName, Class clazz) {
        RequestManager.getInstance(mView.getContext()).placeRequest(methodName, clazz, this, null, false,null);
    }

    public void placeRequest(String methodName, Class clazz, HashMap<String, String> params, boolean isPOST) {
        RequestManager.getInstance(mView.getContext()).placeRequest(methodName, clazz, this, params, isPOST,null);
    }

    public void placeRequest(String methodName, Class clazz, JSONObject jsonObject) {
        RequestManager.getInstance(mView.getContext()).placeRequest(methodName, clazz, this, jsonObject);
    }

    public void placeMultiPartRequest(String methodName, Class clazz, HashMap<String, String> params, File file, String fileKey) {
        RequestManager.getInstance(mView.getContext()).placeMultiPartRequest(methodName, clazz, this, params, file, fileKey);
    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
    }

    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {
    }

    @Override
    public void complete(int code) {

    }


    /**
     * Method to set the User Data for the Params while making the server calls
     *
     * @param params
     */
    public void setUserData(HashMap<String, String> params) {
        params.put(FeedParams.AUTHKEY, SessionManager.getSessionId(getActivity()));
    }

    /**
     * Had to be overridden by the Fragments who wants to save their states
     */
    public StateHolder onSaveViewSettings() {
        StateHolder stateHolder = getStateHolder();
        if (stateHolder == null) {
            stateHolder = new StateHolder(getClass().getName(), getArguments());
            setStateHolder(stateHolder);
        }
        return stateHolder;
    }

    public void showNextScreen(BaseFragment fragment) {
        ((AppBaseActivity) getActivity()).showNextScreen(fragment);
    }

    public void showDefaultNextScreen(BaseFragment fragment) {
        ((AppBaseActivity) getActivity()).showNextScreen(fragment);
    }

    /*
     * This method is called when the fragment that we want to add has to be
     * added as the root fragment, thus clearing the stack
     *
     * @param fragment
     */
    public void showAsRootScreen(BaseFragment fragment) {
        ((AppBaseActivity) getActivity()).showAsRootScreen(fragment);
    }

    public void showAsDefaultRootScreen(BaseFragment fragment) {
        ((AppBaseActivity) getActivity()).showAsRootScreen(fragment);
    }

    public void freeFromLastParent() {
        if (mView != null) {
            ViewGroup v = ((ViewGroup) mView.getParent());
            if (v != null) {
                v.removeAllViews();
            }
        }

        // commented as it is making the fragment lose its properties
        // setStateHolder(null);
    }

    /**
     * Should return true if the fragment has to animate in trasition while
     * appearing or exiting
     *
     * @return
     */
    public boolean shouldAnimate() {
        return false;
    }

    /**
     * keeps updating the state of the view like scrolling position, button
     * clicked etc
     *
     * @return
     */
    public StateHolder getStateHolder() {
        return stateHolder;
    }

    /*
     * Method to set the StateHolder
     */
    public void setStateHolder(StateHolder stateHolder) {
        this.stateHolder = stateHolder;
    }

    /*
     * Method to get the context in the fragment
     *
     * @return Context
     */
    public Context getFragmentContext() {
        return fragmentContext;
    }

    /*
     * Method to set the Context value which will be accessed in the Fragment
     */
    public void setFragmentContext(Context fragmentContext) {
        this.fragmentContext = fragmentContext;
    }

    /*
     * Method called to check if the onBackPressed has been overridden in the
     * fragments
     */
    public boolean onBackPressed() {
        return false;
    }

    public void resetToReload() {
        mView = null;
        getStateHolder().setTag(null);
    }

    /**
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    /**
     * Method to show the Toast message
     *
     * @param stringID
     */
    public void showToast(int stringID) {
        String str = getString(stringID);
        mToast.setText(str);
        mToast.show();
    }

    /**
     * Method to show the Toast message
     *
     * @param str
     */
    public void showToast(String str) {
        mToast.setText(str);
        mToast.show();
    }
}
