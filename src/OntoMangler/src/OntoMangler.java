/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sebmate
 */
public class OntoMangler {
    public static void main(String[] args) {
        OntoManglerForm myForm = new OntoManglerForm();
        myForm.setVisible(true);
        myForm.loadConfiguration();
        myForm.getAndSaveConfiguration();
    }
}
