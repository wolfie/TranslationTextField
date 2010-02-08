package com.github.wolfie.translationtextfield.client.ui;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VTranslationTextField extends Widget implements Paintable {
  
  /** Set the CSS class name to allow styling. */
  public static final String CLASSNAME = "v-translationtextfield";
  
  public static final String V_STRINGS_ARRAY = "strings";
  public static final String V_LOCALES_ARRAY = "locales";
  
  public static final String A_IMMEDIATE_INT = "immediate";
  
  /** The client side widget identifier */
  protected String paintableId;
  
  /** Reference to the server connection object. */
  ApplicationConnection client;
  
  /**
   * The constructor should first call super() to initialize the component and
   * then handle any initialization relevant to Vaadin.
   */
  public VTranslationTextField() {
    setElement(Document.get().createDivElement());
    setStyleName(CLASSNAME);
  }
  
  /**
   * Called whenever an update is received from the server
   */
  public void updateFromUIDL(final UIDL uidl, final ApplicationConnection client) {
    // This call should be made first.
    // It handles sizes, captions, tooltips, etc. automatically.
    if (client.updateComponent(this, uidl, true)) {
      // If client.updateComponent returns true there has been no changes and we
      // do not need to update anything.
      return;
    }
    
    // Save reference to server connection object to be able to send
    // user interaction later
    this.client = client;
    
    // Save the client side identifier (paintable id) for the widget
    paintableId = uidl.getId();
    
    // TODO replace dummy code with actual component logic
    getElement().setInnerHTML("It works!");
    
  }
  
}
