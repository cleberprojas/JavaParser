package com.ef;

import com.ef.handler.ParserHandler;

/**
 *
 */
public class Parser{

  public static void main( String[] args ){
    ParserHandler handler = new ParserHandler();
    handler.handleLogFile(args);
  }
  
}
