package com.github.wolfie.translationtextfield;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import com.github.wolfie.translationtextfield.client.ui.VTranslationTextField;
import com.vaadin.data.Property;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.ClientWidget;

/**
 * A text field that can be used to input multilingual entries in one go.
 * 
 * @author Henrik Paul
 */
@ClientWidget(VTranslationTextField.class)
public class TranslationTextField extends AbstractField {
  
  public enum Immediateness {
    /** */
    NONE,

    /** */
    NORMAL,

    /** */
    PER_ENTRY
  };
  
  private static final long serialVersionUID = 7401269973380055307L;
  private static final ThreadLocal<List<Locale>> LOCALE_ORDER_STACK = new ThreadLocal<List<Locale>>();
  
  private Immediateness immediateMode = Immediateness.NONE;
  
  static {
    LOCALE_ORDER_STACK.set(Arrays.asList(Locale.getDefault()));
  }
  
  public TranslationTextField() {
    this(TranslatedString.EMPTY);
  }
  
  public TranslationTextField(final Property property) {
    this(TranslatedString.EMPTY);
    setPropertyDataSource(property);
  }
  
  public TranslationTextField(final TranslatedString string) {
    setValue(string);
  }
  
  @Override
  public void paintContent(final PaintTarget target) throws PaintException {
    super.paintContent(target);
    
    final TranslatedString string = getValueAsTranslatedString();
    final List<String> strings = new ArrayList<String>(string.size());
    final List<String> locales = new ArrayList<String>(string.size());
    
    for (final Entry<Locale, String> entry : string.toMap().entrySet()) {
      strings.add(entry.getValue());
      locales.add(entry.getKey().toString());
    }
    
    target.addVariable(this, VTranslationTextField.V_STRINGS_ARRAY, strings
        .toArray(new String[strings.size()]));
    target.addVariable(this, VTranslationTextField.V_LOCALES_ARRAY, locales
        .toArray(new String[locales.size()]));
    
    target.addAttribute(VTranslationTextField.A_IMMEDIATE_INT, immediateMode
        .ordinal());
  }
  
  private TranslatedString getValueAsTranslatedString() {
    return (TranslatedString) getValue();
  }
  
  @Override
  public Class<?> getType() {
    return TranslatedString.class;
  }
  
  public static void setLocales(final List<Locale> locales) {
    LOCALE_ORDER_STACK.set(Collections.unmodifiableList(new ArrayList<Locale>(
        locales)));
  }
  
  /**
   * An unmodifiable list of the currently used locales.
   * 
   * @return
   */
  public static List<Locale> getLocales() {
    return LOCALE_ORDER_STACK.get();
  }
  
  public static Locale getPreviewLocale() {
    return LOCALE_ORDER_STACK.get().get(0);
  }
  
  @Override
  public void setValue(final Object newValue) throws ReadOnlyException,
      ConversionException {
    setValue(newValue, false);
  }
  
  @Override
  protected void setValue(final Object newValue,
      final boolean repaintIsNotNeeded) throws ReadOnlyException,
      ConversionException {
    
    if (newValue == null || newValue instanceof TranslatedString) {
      // gobble translated strings as they are
      super.setValue(newValue, repaintIsNotNeeded);
    }

    else if (newValue instanceof CharSequence || newValue instanceof char[]
        || newValue instanceof Character[]) {
      // if it's string-like, set it as the default translation's value.
      final Locale defaultLocale = getPreviewLocale();
      final String string = newValue.toString();
      final TranslatedString translatedString = new TranslatedString(
          defaultLocale, string);
      super.setValue(translatedString, repaintIsNotNeeded);
    }

    else {
      throw new Property.ConversionException(
          "Value must be either an instance of "
              + TranslatedString.class.getName() + " or String-like.");
    }
  }
  
  @Override
  public boolean isImmediate() {
    return immediateMode != Immediateness.NONE;
  }
  
  @Override
  public void setImmediate(final boolean immediate) {
    if (immediate) {
      setImmediate(Immediateness.NORMAL);
    } else {
      setImmediate(Immediateness.NONE);
    }
  }
  
  public void setImmediate(final Immediateness immediateness) {
    if (immediateness != null) {
      immediateMode = immediateness;
    } else {
      throw new NullPointerException("immediateness cannot be null");
    }
  }
}
