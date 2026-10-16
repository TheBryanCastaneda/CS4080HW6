package com.craftinginterpreters.lox;

import java.util.List;

class LoxInner implements LoxCallable {
  private final LoxFunction method;
  private final LoxInstance receiver;

  LoxInner(LoxFunction method, LoxInstance receiver) {
    this.method = method;
    this.receiver = receiver;
  }

  @Override
  public int arity() {
    return method == null ? -1 : method.arity();
  }

  @Override
  public Object call(
      Interpreter interpreter,
      List<Object> arguments) {
    if (method == null) {
      return null;
    }

    return method.bind(receiver).call(interpreter, arguments);
  }

  @Override
  public String toString() {
    return "<fn inner>";
  }
}