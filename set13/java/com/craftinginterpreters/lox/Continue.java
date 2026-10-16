package com.craftinginterpreters.lox;

class Continue extends RuntimeException {
  private static final long serialVersionUID = 1L;

  Continue() {
    super(null, null, false, false);
  }
}