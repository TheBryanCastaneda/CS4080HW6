package com.craftinginterpreters.lox;

import java.util.List;

class LoxFunction implements LoxCallable {
  private final String name;
  private final List<Token> params;
  private final List<Stmt> body;
  private final Environment closure;
  private final boolean isInitializer;
  final boolean isGetter;

  LoxFunction(
      String name,
      List<Token> params,
      List<Stmt> body,
      Environment closure) {
    this(name, params, body, closure, false, false);
  }

  LoxFunction(
      String name,
      List<Token> params,
      List<Stmt> body,
      Environment closure,
      boolean isInitializer,
      boolean isGetter) {
    this.name = name;
    this.params = params;
    this.body = body;
    this.closure = closure;
    this.isInitializer = isInitializer;
    this.isGetter = isGetter;
  }

  LoxFunction bind(LoxInstance instance) {
    Environment environment = new Environment(closure);
    environment.defineLocal(instance);
    return new LoxFunction(
        name, params, body, environment,
        isInitializer, isGetter);
  }

  @Override
  public int arity() {
    return params.size();
  }

  @Override
  public Object call(
      Interpreter interpreter,
      List<Object> arguments) {
    Environment environment = new Environment(closure);

    for (int i = 0; i < params.size(); i++) {
      environment.defineLocal(arguments.get(i));
    }

    try {
      interpreter.executeBlock(body, environment);
    } catch (Return returnValue) {
      if (isInitializer) {
        return closure.getAt(0, 0);
      }

      return returnValue.value;
    }

    if (isInitializer) {
      return closure.getAt(0, 0);
    }

    return null;
  }

  @Override
  public String toString() {
    if (name == null) {
      return "<fn anonymous>";
    }

    return "<fn " + name + ">";
  }
}