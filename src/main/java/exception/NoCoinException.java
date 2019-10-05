package exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NoCoinException extends RuntimeException {

  public NoCoinException(String message){
    super(message);

  }

}
