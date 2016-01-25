package com.outlookgroup.outlookmags.manager;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.outlookgroup.outlookmags.R;
import com.outlookgroup.outlookmags.fragments.BaseFragment;

import java.util.ArrayList;

public class PageManager {
    private FragmentActivity activity;
    private ArrayList<BaseFragment> fragmentQueue = new ArrayList<BaseFragment>();
    private ArrayList<StateHolder> stateQueue = new ArrayList<StateHolder>();
    private int pageLimit;

    public PageManager(FragmentActivity activity) {
        this.setActivity(activity);
        this.pageLimit = 2;
    }

    public PageManager(FragmentActivity activity, int pageLimit) {
        this.setActivity(activity);
        this.pageLimit = pageLimit;
    }

    public FragmentActivity getActivity() {
        return activity;
    }

    public void setActivity(FragmentActivity activity) {
        this.activity = activity;
    }

    public ArrayList<BaseFragment> getFragmentQueue() {
        return fragmentQueue;
    }

    public void setFragmentQueue(ArrayList<BaseFragment> fragmentQueue) {
        this.fragmentQueue = fragmentQueue;
    }

    public int getPageLimit() {
        return pageLimit;
    }

    public void setPageLimit(int pageLimit) {
        this.pageLimit = pageLimit;
    }

    private void clearAllScreens() {
        fragmentQueue.clear();
        stateQueue.clear();

    }

    public void pushAsRootScreen(BaseFragment baseFragment) {
        baseFragment.setFragmentContext(getActivity());
        clearAllScreens();
        StateHolder stateHolder = baseFragment.onSaveViewSettings();
        stateHolder.setInLoggedInState(isUserLoggedIn());
        stateHolder.setRootItem(true);
        addState(stateHolder);
        addToRecent(baseFragment);
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_container, baseFragment);
        ft.commitAllowingStateLoss();
    }

    public void pushScreen(BaseFragment baseFragment) {
        baseFragment.setFragmentContext(getActivity());
        StateHolder stateHolder = baseFragment.onSaveViewSettings();
        stateHolder.setInLoggedInState(isUserLoggedIn());
        addState(stateHolder);
        if (hasReachedMaxLimit()) {
            removeFromOldest();
        }
        addToRecent(baseFragment);

        FragmentTransaction ft = getActivity().getSupportFragmentManager()
                .beginTransaction();
        if (baseFragment.shouldAnimate()) {
            //ft.setCustomAnimations(R.anim.slide_enter, R.anim.slide_exit);
        }
        ft.replace(R.id.frame_container, baseFragment);
        // Fix added so that we can switch to fragments right after an OnActivityResult Call
        ft.commitAllowingStateLoss();
    }

    private void addState(StateHolder stateHolder) {
        stateQueue.add(stateHolder);
    }

    public void setTagToPreviousState(Object tag) {
        if (stateQueue != null && stateQueue.size() > 1
                && stateQueue.get(stateQueue.size() - 2) != null) {
            StateHolder state = stateQueue.get(stateQueue.size() - 2);
            state.setTag(tag);
        }

    }

    public Object getTagFromPreviousState() {
        if (stateQueue != null && stateQueue.size() > 1
                && stateQueue.get(stateQueue.size() - 2) != null) {
            StateHolder state = stateQueue.get(stateQueue.size() - 2);
            return state.getTag();
        }
        return null;
    }

    public boolean popScreen() {
        boolean popped = false;
        BaseFragment mostRecentlyVisited = removeFromRecent();
        BaseFragment lastVisited = getMostRecent();

        StateHolder previousState = getStateForClass(lastVisited.getClass()
                .getName());
        if (previousState != null)
            lastVisited.setStateHolder(previousState);
        FragmentTransaction ft = getActivity().getSupportFragmentManager()
                .beginTransaction();
        if (mostRecentlyVisited.shouldAnimate()) {
            //ft.setCustomAnimations(R.anim.slide_in_fromleft, R.anim.slide_out_fromright);
        }
        ft.replace(R.id.frame_container, lastVisited);
        ft.commitAllowingStateLoss();
        if (lastVisited != null) {
            popped = true;
        }
        return popped;
    }

    public boolean hasFragments() {
        return fragmentQueue.size() > 0;
    }

    private BaseFragment removeFromOldest() {
        return hasFragments() ? fragmentQueue.remove(0) : null;
    }

    private BaseFragment removeFromRecent() {
        if (stateQueue.size() > 0) {
            stateQueue.remove(stateQueue.size() - 1);
        }
        return hasFragments() ? fragmentQueue.remove(fragmentQueue.size() - 1)
                : null;
    }

    public BaseFragment getMostRecent() {
        return fragmentQueue.size() > 0 ? fragmentQueue.get(fragmentQueue
                .size() - 1) : null;
    }

    private void addToRecent(BaseFragment baseFragment) {
        fragmentQueue.add(baseFragment);
    }

    private void addToOldest(BaseFragment baseFragment) {
        fragmentQueue.add(0, baseFragment);
    }

    private boolean hasReachedMaxLimit() {
        return fragmentQueue.size() >= getPageLimit();
    }

    public boolean onBackPressed() {
        boolean handled = false;
        BaseFragment mostRecent = getMostRecent();
        if (!hasLastScreen()) {
            if (mostRecent != null) {
                handled = mostRecent.onBackPressed();
                if (!handled) {
                    // Check if the login state has changed
                    StateHolder previousScreenState = getStateFor(currentPageIndex() - 1);
                    boolean stateHasChanged = (mostRecent.getStateHolder()
                            .isInLoggedInState() != previousScreenState
                            .isInLoggedInState());
                    if (stateHasChanged) {
                        // update the new state
                        previousScreenState.setInLoggedInState(mostRecent
                                .getStateHolder().isInLoggedInState());
                    }
                    BaseFragment previousScreen = getPreviousScreen();
                    // check if the previous screen is cached
                    if (previousScreen == null) {
                        // previous screen is not cached
                        // restoring the previous view
                        previousScreen = createFragmentFromState(currentPageIndex() - 1);
                        previousScreen.setFragmentContext(getActivity());
                        addToOldest(previousScreen);
                    } else {
                        // update the new state to the previous screen
                        if (stateHasChanged) {
                            previousScreen.resetToReload();
                        }
                    }
                    handled = popScreen();
                }
            }
        } else {
            if (mostRecent != null) {
                handled = mostRecent.onBackPressed();
            }
        }
        return handled;
    }

    private BaseFragment getPreviousScreen() {
        return (fragmentQueue.size() > 1) ? fragmentQueue.get(fragmentQueue
                .size() - 2) : null;
    }

    public int currentPageIndex() {
        return (stateQueue.size() - 1);
    }

    private BaseFragment createFragmentFromState(int stateIndex) {
        return (stateIndex <= currentPageIndex()) ? getStateFor(stateIndex).getSavedFragment() : null;
    }

    private StateHolder getStateFor(int index) {
        return (index < stateQueue.size()) ? stateQueue.get(index) : null;
    }

    /**
     * @param fragmentClassName
     * @return
     */
    private StateHolder getStateForClass(String fragmentClassName) {
        for (int i = stateQueue.size() - 1; i > 0; i--) {
            if (stateQueue.get(i).getFragmentName().equals(fragmentClassName)) {
                return stateQueue.get(i);
            }
        }
        return null;

    }

    private boolean hasLastScreen() {
        return stateQueue.size() == 1;
    }

    public ArrayList<StateHolder> getStateQueue() {
        return stateQueue;
    }

    public void setStateQueue(ArrayList<StateHolder> stateQueue) {
        this.stateQueue = stateQueue;
    }

    public void popUntilScreen(String fragmentClassName) {
        try {
            while (removeFromRecent().getStateHolder().getClass()
                    .equals(fragmentClassName)) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isUserLoggedIn() {
        return (SessionManager.getSessionId(getActivity()) != null);
    }

    public void onResume() {
        BaseFragment fragment = getMostRecent();
        if (fragment != null) {
            StateHolder stateHolder = fragment.getStateHolder();
            boolean isLoggedIn = isUserLoggedIn();
            if (stateHolder.isInLoggedInState() != isLoggedIn) {
                stateHolder.setInLoggedInState(isLoggedIn);
                popImmediate();
                pushScreenFromState(stateHolder);
            }
        }
    }

    public boolean popImmediate() {
        boolean popped = false;
        BaseFragment lastVisited = removeFromRecent();
        if (lastVisited != null) {
            popped = true;
        }
        return popped;
    }

    public void pushScreenFromState(StateHolder stateHolder) {
        BaseFragment BaseFragment = stateHolder.getSavedFragment();
        BaseFragment.setFragmentContext(getActivity());
        BaseFragment.setStateHolder(stateHolder);
        addState(stateHolder);
        if (hasReachedMaxLimit()) {
            removeFromOldest();
        }
        addToRecent(BaseFragment);
        FragmentTransaction ft = getActivity().getSupportFragmentManager()
                .beginTransaction();
        ft.replace(R.id.frame_container, BaseFragment);
        // Fix added so that we can switch to fragments right after an OnActivityResult Call
        ft.commitAllowingStateLoss();
    }
}
