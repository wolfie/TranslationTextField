package com.github.wolfie.translationtextfield;

import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;

/**
 * An immutable multilingual string.
 * 
 * @author Henrik Paul
 */
public final class TranslatedString {
  public static final class Builder {
    private final Map<Locale, String> entries = Maps.newHashMap();
    
    private Builder() {
      // API stays cleaner without runaway builder-creations. Let them be
      // created via the with()-methods
    }
    
    /**
     * <p>
     * Add a locale/string combination to the builder.
     * </p>
     * <p>
     * If the given locale has previously been given to the builder, it will be
     * overwritten.
     * </p>
     * 
     * @param locale
     *          A non-<tt>null</tt> {@link Locale}
     * @param string
     *          A non-<tt>null</tt> {@link String}
     * @return
     * @throws NullPointerException
     *           if any argument is <tt>null</tt>.
     */
    public Builder and(final Locale locale, final String string)
        throws NullPointerException {
      entries.put(checkNotNull(locale), checkNotNull(string));
      return this;
    }
    
    public TranslatedString create() {
      return new TranslatedString(entries);
    }
    
    private Builder and(final Map<Locale, String> map) {
      // No need to check for nulls, since this is handled by the calling
      // methods. If this method gets turned into public, the nulls must be
      // checked.
      entries.putAll(map);
      return this;
    }
  }
  
  public static final TranslatedString EMPTY = new TranslatedString();
  
  private final ImmutableMap<Locale, String> strings;
  
  private TranslatedString() {
    strings = ImmutableMap.of();
  }
  
  /**
   * <p>
   * Create a {@link TranslatedString} with one translation.
   * </p>
   * <p>
   * <em>Note:</em> If you need to create a {@link TranslatedString} with more
   * than one translation, see {@link #TranslatedString(Map)},
   * {@link #with(Locale, String)} and {@link #with(Map)}
   * 
   * @param locale
   * @param string
   */
  public TranslatedString(final Locale locale, final String string) {
    strings = ImmutableMap.of(checkNotNull(locale), checkNotNull(string));
  }
  
  public TranslatedString(final Map<Locale, String> map) {
    strings = ImmutableMap.copyOf(checkNotNull(map));
  }
  
  public static Builder with(final Locale locale, final String string) {
    final Builder builder = new Builder();
    builder.and(locale, string);
    return builder;
  }
  
  public static Builder with(final Map<Locale, String> map) {
    final Builder builder = new Builder();
    builder.and(map);
    return builder;
  }
  
  public TranslatedString set(final Locale locale, final String string) {
    return TranslatedString.with(strings).and(locale, string).create();
  }
  
  public TranslatedString unset(final Locale locale) {
    final Predicate<Locale> localeFilterPredicate = new Predicate<Locale>() {
      public boolean apply(final Locale input) {
        return !locale.equals(input);
      }
    };
    
    final TranslatedString translatableString = new TranslatedString(Maps
        .filterKeys(strings, localeFilterPredicate));
    return translatableString;
  }
  
  public ImmutableMap<Locale, String> toMap() {
    return strings;
  }
  
  @Override
  public int hashCode() {
    return strings.hashCode();
  }
  
  @Override
  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    } else if (o instanceof TranslatedString) {
      return strings.equals(((TranslatedString) o).strings);
    } else {
      return false;
    }
  }
  
  @Override
  public String toString() {
    return strings.toString();
  }
  
  public String toString(final Locale locale) {
    return String.valueOf(strings.get(locale));
  }
  
  public boolean isSet(final Locale locale) {
    return strings.containsKey(locale);
  }
  
  public ImmutableSet<Locale> getLocales() {
    return strings.keySet();
  }
  
  private static <K extends Object, V extends Object> Map<K, V> checkNotNull(
      final Map<K, V> map) {
    for (final Entry<K, V> entry : map.entrySet()) {
      final K key = entry.getKey();
      final V value = entry.getValue();
      if (key == null) {
        throw new NullPointerException(
            "Map contained null-key, which is not allowed. Its value was: "
                + value);
      }
      if (value == null) {
        throw new NullPointerException(
            "Map contained null-value, which isn't allowed. Its key was: "
                + key);
      }
    }
    return map;
  }
  
  private static <T extends Object> T checkNotNull(final T o) {
    if (o != null) {
      return o;
    } else {
      throw new NullPointerException("Value may not be null");
    }
  }
  
  /**
   * The amount of locale/string pairs.
   * 
   * @return
   */
  public int size() {
    return strings.size();
  }
}
