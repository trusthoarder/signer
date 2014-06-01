package main.java.com.trusthoarder.signer.infrastructure;

public abstract class Optional<TYPE> {
  private static final Optional NONE = new Optional(){

    @Override
    public Object get() {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean isPresent() {
      return false;
    }

    @Override
    public Optional or( Optional secondChoice ) {
      return secondChoice;
    }

    @Override
    public Optional or( Object secondChoice ) {
      return some( secondChoice );
    }
  };

  public static <TYPE> Optional<TYPE> none()
  {
    return NONE;
  }

  public static <TYPE> Optional<TYPE> some( final TYPE obj )
  {
    return new Optional<TYPE>(){

      @Override
      public TYPE get() {
        return obj;
      }

      @Override
      public boolean isPresent() {
        return true;
      }

      @Override
      public Optional<TYPE> or( Optional<? extends TYPE> secondChoice ) {
        return this;
      }

      @Override
      public Optional<TYPE> or( TYPE secondChoice ) {
        return this;
      }
    };
  }

  public abstract TYPE get();
  public abstract boolean isPresent();

  public abstract Optional<TYPE> or(Optional<? extends TYPE> secondChoice);
  public abstract Optional<TYPE> or(TYPE secondChoice);
}
