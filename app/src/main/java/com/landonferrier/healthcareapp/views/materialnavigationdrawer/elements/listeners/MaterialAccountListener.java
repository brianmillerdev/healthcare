package com.landonferrier.healthcareapp.views.materialnavigationdrawer.elements.listeners;

import com.landonferrier.healthcareapp.views.materialnavigationdrawer.elements.MaterialAccount;


/**
 * Created by neokree on 11/12/14.
 */
public interface MaterialAccountListener {

    public void onAccountOpening(MaterialAccount account);

    public void onChangeAccount(MaterialAccount newAccount);

}
