package com.app.outlook.manager;

import android.os.Bundle;

import com.app.outlook.fragments.BaseFragment;

import java.util.HashMap;


public class StateHolder {

    private String fragmentName;
    private Bundle bundle;
    private HashMap<String, String> specificValues;
    private Object tag;
    private boolean inloggedInState;
    private boolean rootItem;

    public StateHolder() {
        // TODO Auto-generated constructor stub
    }

    public StateHolder(String fragmentName, Bundle bundle) {
        this.fragmentName = fragmentName;
        this.bundle = bundle;
    }

    public String getFragmentName() {
        return fragmentName;
    }

    public void setFragmentName(String fragmentName) {
        this.fragmentName = fragmentName;
    }

    public BaseFragment getSavedFragment() {
        BaseFragment savedFragment = null;
        try {
            Class<?> savedclass;
            savedclass = Class.forName(fragmentName);
            savedFragment = (BaseFragment) savedclass.newInstance();
            savedFragment.setArguments(getBundle());
            savedFragment.setStateHolder(this);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return savedFragment;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public HashMap<String, String> getSpecificValues() {
        return specificValues;
    }

    public void setSpecificValues(HashMap<String, String> specificValues) {
        this.specificValues = specificValues;
    }

    public void updateSpecificValue(String key, String value) {
        if (specificValues == null) {
            specificValues = new HashMap<String, String>();
        }
        specificValues.put(key, value);
    }

    public String getSpecificValue(String key) {
        if (specificValues != null) {
            return specificValues.get(key);
        }
        return null;
    }

    /**
     * @return the tag
     */
    public Object getTag() {
        return tag;
    }

    /**
     * @param tag the tag to set
     */
    public void setTag(Object tag) {
        this.tag = tag;
    }

    public boolean isInLoggedInState() {
        return inloggedInState;
    }

    public void setInLoggedInState(boolean inloggedInState) {
        this.inloggedInState = inloggedInState;
    }

    public boolean isRootItem() {
        return rootItem;
    }

    public void setRootItem(boolean rootItem) {
        this.rootItem = rootItem;
    }

}
