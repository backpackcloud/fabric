/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2025 Marcelo "Ataxexe" Guimarães
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.backpackcloud;

import java.io.Serial;
import java.util.function.Supplier;

/// Legend says this exception raises from the deepness circuits, usually awaken by
/// digital incarnations of Murphy.
///
/// Catching this exception may lead to unrecoverable systems, memory leak, data leak,
/// water leak and butt pain.
///
/// @author Ataxexe
public class UnbelievableException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = -5430796388952219805L;

  /// Creates the ultimate armageddon. Where no single clue is present,
  /// and doom is the only fate for us all.
  ///
  /// Usually this is a sign that all hope is lost, and you should get cover
  /// as soon as possible.
  ///
  /// @see Exception#Exception()
  public UnbelievableException() {
  }

  /// Conjures the apocalypse by passing a message for its ancestors in a desperate
  /// move of forgiveness and humility.
  ///
  /// @param message your last pray before the end
  /// @see Exception#Exception(String)
  public UnbelievableException(String message) {
    super(message);
  }

  /// Offers a mere throwable for the Kraken of the bit sea, alongside with a plea.
  ///
  /// Do not expect anything good in return. You're on your own.
  ///
  /// @param message your plea
  /// @param cause   your offer
  /// @see Exception#Exception(String, Throwable)
  public UnbelievableException(String message, Throwable cause) {
    super(message, cause);
  }

  /// Be bold and offer just a single piece of throwable. Your survival chances are
  /// lower, but the efforts are also significantly lower. What would you expect?
  ///
  /// @param cause your offer, it better be good
  /// @see Exception#Exception(Throwable)
  public UnbelievableException(Throwable cause) {
    super(cause);
  }

  /// Ok, you can try and win by tossing some bureaucracy to the event horizon. Maybe the void
  /// will spare your life.
  ///
  /// But your app is broken either way.
  ///
  /// @param message            your plea, make it beautiful and increase your lifespan for at max 42 picoseconds
  /// @param cause              your offer, it doesn't matter if it's covered with gold, there's enough in the motherboard
  /// @param enableSuppression  go on and try to disable suppression
  /// @param writableStackTrace disable writing to stacktrace if you dare
  /// @see Exception#Exception(String, Throwable, boolean, boolean)
  public UnbelievableException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  /// Throws the crap out of sight, aiming on the fan while leaving time for you to take cover in your toilet.
  ///
  /// @param reason the reason for all the madness that's preventing your code from causing even more damage.
  /// @see #UnbelievableException(String)
  public static Supplier<UnbelievableException> because(String reason) {
    return () -> new UnbelievableException(reason);
  }

}
