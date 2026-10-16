package com.craftinginterpreters.lox;

import java.util.List;
import java.util.Map;

class LoxClass extends LoxInstance implements LoxCallable {
  final String name;
  final LoxClass superclass;
  private final Map<String, LoxFunction> methods;

  LoxClass(
      LoxClass metaclass,
      String name,
      LoxClass superclass,
      Map<String, LoxFunction> methods) {
    super(metaclass);
    this.name = name;
    this.superclass = superclass;
    this.methods = methods;

    for (LoxFunction method : methods.values()) {
      method.owner = this;
    }
  }

  LoxFunction findMethod(String name) {
    if (superclass != null) {
      LoxFunction inherited = superclass.findMethod(name);

      if (inherited != null) {
        return inherited;
      }
    }

    return methods.get(name);
  }

  LoxFunction findInnerMethod(LoxClass owner, String name) {
    LoxClass current = this;
    LoxFunction method = null;

    while (current != null && current != owner) {
      LoxFunction candidate = current.methods.get(name);

      if (candidate != null) {
        method = candidate;
      }

      current = current.superclass;
    }

    if (current == null) {
      return null;
    }

    return method;
  }

  @Override
  public int arity() {
    LoxFunction initializer = findMethod("init");
    return initializer == null ? 0 : initializer.arity();
  }

  @Override
  public Object call(
      Interpreter interpreter,
      List<Object> arguments) {
    LoxInstance instance = new LoxInstance(this);
    LoxFunction initializer = findMethod("init");

    if (initializer != null) {
      initializer.bind(instance).call(interpreter, arguments);
    }

    return instance;
  }

  @Override
  public String toString() {
    return name;
  }
}