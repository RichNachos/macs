from typing import Iterable, List

from n2t.core.compiler.jack_tokenizer import JackTokenizer, Token

OPERATORS = ["&lt;", "=", "&gt;", "*", "/", "+", "-", "&amp;", "|"]
INDENT_SIZE = 2
INDENT = " " * INDENT_SIZE


class CompilationEngine:
    def __init__(self, inp: Iterable[str]) -> None:
        self.tokenizer: JackTokenizer = JackTokenizer(inp)
        self.indent: int = 0
        self.out: List[str] = []

    def compile(self) -> List[str]:
        self.compile_class()
        return self.out

    def compile_class(self) -> None:
        if self.tokenizer.has_more_tokens():
            self.tokenizer.advance()
            tag = "class"
            self.start_tag(tag)
            self.write_keyword()
            self.tokenizer.advance()
            self.write_identifier()
            self.tokenizer.advance()
            self.write_symbol()
            self.tokenizer.advance()
            while self.tokenizer.token in ["static", "field"]:
                self.compile_class_var_dec()
            while self.tokenizer.token in ["constructor", "function", "method"]:
                self.compile_subroutine()
            self.write_symbol()
            self.end_tag(tag)

    def compile_class_var_dec(self) -> None:
        tag = "classVarDec"
        self.start_tag(tag)
        self.write_keyword()
        self.tokenizer.advance()
        self.compile_type_and_var_name()
        self.end_tag(tag)

    def compile_subroutine(self) -> None:
        tag = "subroutineDec"
        self.start_tag(tag)
        self.write_keyword()
        self.tokenizer.advance()
        if self.tokenizer.token_type == Token.KEYWORD:
            self.write_keyword()
        if self.tokenizer.token_type == Token.IDENTIFIER:
            self.write_identifier()
        self.tokenizer.advance()
        self.write_identifier()
        self.tokenizer.advance()
        self.write_symbol()
        self.tokenizer.advance()
        self.compile_parameter_list()
        self.write_symbol()
        self.tokenizer.advance()
        self.compile_subroutine_body()
        self.end_tag(tag)
        self.tokenizer.advance()

    def compile_parameter_list(self) -> None:
        tag = "parameterList"
        self.start_tag(tag)
        while self.tokenizer.token_type != Token.SYMBOL:
            if self.tokenizer.token_type == Token.KEYWORD:
                self.write_keyword()
            if self.tokenizer.token_type == Token.IDENTIFIER:
                self.write_identifier()
            self.tokenizer.advance()
            self.write_identifier()
            self.tokenizer.advance()
            if self.tokenizer.token in ",":
                self.write_symbol()
                self.tokenizer.advance()
        self.end_tag(tag)

    def compile_subroutine_body(self) -> None:
        tag = "subroutineBody"
        self.start_tag(tag)
        self.write_symbol()
        self.tokenizer.advance()
        while self.tokenizer.token == "var":
            self.compile_var_dec()
        self.compile_statements()
        self.write_symbol()
        self.end_tag(tag)

    def compile_var_dec(self) -> None:
        tag = "varDec"
        self.start_tag(tag)
        self.write_keyword()
        self.tokenizer.advance()
        self.compile_type_and_var_name()
        self.end_tag(tag)

    def compile_statements(self) -> None:
        tag = "statements"
        self.start_tag(tag)
        while self.tokenizer.token_type == Token.KEYWORD:
            match self.tokenizer.token:
                case "let":
                    self.compile_let()
                case "if":
                    self.compile_if()
                case "while":
                    self.compile_while()
                case "do":
                    self.compile_do()
                case "return":
                    self.compile_return()
        self.end_tag(tag)

    def compile_let(self) -> None:
        tag = "letStatement"
        self.start_tag(tag)
        self.write_keyword()
        self.tokenizer.advance()
        self.write_identifier()
        self.tokenizer.advance()
        if self.tokenizer.token in "[":
            self.write_symbol()
            self.tokenizer.advance()
            self.compile_expression()
            self.write_symbol()
            self.tokenizer.advance()
        self.write_symbol()
        self.tokenizer.advance()
        self.compile_expression()
        self.write_symbol()
        self.end_tag(tag)
        self.tokenizer.advance()

    def compile_if(self) -> None:
        tag = "ifStatement"
        self.start_tag(tag)
        self.write_keyword()
        self.tokenizer.advance()
        self.write_symbol()
        self.tokenizer.advance()
        self.compile_expression()
        self.write_symbol()
        self.tokenizer.advance()
        self.write_symbol()
        self.tokenizer.advance()
        self.compile_statements()
        self.write_symbol()

        self.tokenizer.advance()
        if (
            self.tokenizer.token_type == Token.KEYWORD
            and self.tokenizer.token == "else"
        ):
            self.write_keyword()
            self.tokenizer.advance()
            self.write_symbol()
            self.tokenizer.advance()
            self.compile_statements()
            self.write_symbol()
            self.tokenizer.advance()
        self.end_tag(tag)

    def compile_while(self) -> None:
        tag = "whileStatement"
        self.start_tag(tag)
        self.write_keyword()
        self.tokenizer.advance()
        self.write_symbol()
        self.tokenizer.advance()
        self.compile_expression()
        self.write_symbol()
        self.tokenizer.advance()
        self.write_symbol()
        self.tokenizer.advance()
        self.compile_statements()
        self.write_symbol()
        self.end_tag(tag)
        self.tokenizer.advance()

    def compile_do(self) -> None:
        tag = "doStatement"
        self.start_tag(tag)
        self.write_keyword()
        self.tokenizer.advance()
        self.write_identifier()
        self.tokenizer.advance()
        if self.tokenizer.token in ".":
            self.write_symbol()
            self.tokenizer.advance()
            self.write_identifier()
            self.tokenizer.advance()
        self.write_symbol()
        self.tokenizer.advance()
        self.compile_expression_list()
        self.write_symbol()
        self.tokenizer.advance()
        self.write_symbol()
        self.end_tag(tag)
        self.tokenizer.advance()

    def compile_return(self) -> None:
        tag = "returnStatement"
        self.start_tag(tag)
        self.write_keyword()
        self.tokenizer.advance()
        if self.tokenizer.token_type != Token.SYMBOL and self.tokenizer.token != ";":
            self.compile_expression()
        self.write_symbol()
        self.end_tag(tag)
        self.tokenizer.advance()

    def compile_expression(self) -> None:
        tag = "expression"
        self.start_tag(tag)
        self.compile_term()
        while (
            self.tokenizer.token_type == Token.SYMBOL
            and self.tokenizer.token in OPERATORS
        ):
            self.write_symbol()
            self.tokenizer.advance()
            self.compile_term()
        self.end_tag(tag)

    def compile_term(self) -> None:
        tag = "term"
        self.start_tag(tag)
        advance = True
        match self.tokenizer.token_type:
            case Token.INT_CONST:
                self.write_int()
            case Token.STR_CONST:
                self.write_str()
            case Token.KEYWORD:
                self.write_keyword()
            case Token.IDENTIFIER:
                self.write_identifier()

                self.tokenizer.advance()
                advance = False
                if self.tokenizer.token in "[(":
                    advance = True
                    self.compile_brackets()
                elif self.tokenizer.token == ".":
                    advance = True
                    self.write_symbol()
                    self.tokenizer.advance()
                    self.write_identifier()
                    self.tokenizer.advance()
                    self.write_symbol()
                    self.tokenizer.advance()
                    self.compile_expression_list()
                    self.write_symbol()
            case _:
                if self.tokenizer.token == "(":
                    self.compile_brackets()
                elif self.tokenizer.token in "~-":
                    self.write_symbol()
                    self.tokenizer.advance()
                    self.compile_term()
                    advance = False

        if advance:
            self.tokenizer.advance()

        self.end_tag(tag)

    def compile_brackets(self) -> None:
        self.write_symbol()
        self.tokenizer.advance()
        self.compile_expression()
        self.write_symbol()

    def start_tag(self, tag: str) -> None:
        self.out += [INDENT * self.indent + f"<{tag}>"]
        self.indent += 1

    def end_tag(self, tag: str) -> None:
        self.indent -= 1
        self.out += [INDENT * self.indent + f"</{tag}>"]

    def compile_expression_list(self) -> None:
        tag = "expressionList"
        self.start_tag(tag)

        if self.tokenizer.token_type != Token.SYMBOL and self.tokenizer.token != ")":
            self.compile_expression()
            while (
                self.tokenizer.token_type == Token.SYMBOL
                and self.tokenizer.token == ","
            ):
                self.write_symbol()
                self.tokenizer.advance()
                self.compile_expression()
        if self.tokenizer.token == "(":
            self.compile_expression()
            while (
                self.tokenizer.token_type == Token.SYMBOL
                and self.tokenizer.token == ","
            ):
                self.write_symbol()
                self.tokenizer.advance()
                self.compile_expression()
        self.end_tag(tag)

    def write_keyword(self) -> None:
        self.out += [
            INDENT * self.indent + f"<keyword> {self.tokenizer.token} </keyword>"
        ]

    def write_symbol(self) -> None:
        self.out += [
            INDENT * self.indent + f"<symbol> {self.tokenizer.token} </symbol>"
        ]

    def write_int(self) -> None:
        self.out += [
            INDENT * self.indent
            + f"<integerConstant> {self.tokenizer.token} </integerConstant>"
        ]

    def write_str(self) -> None:
        self.out += [
            INDENT * self.indent
            + f"<stringConstant> {self.tokenizer.token} </stringConstant>"
        ]

    def write_identifier(self) -> None:
        self.out += [
            INDENT * self.indent + f"<identifier> {self.tokenizer.token} </identifier>"
        ]

    def compile_type_and_var_name(self) -> None:
        if self.tokenizer.token_type == Token.KEYWORD:
            self.write_keyword()
        elif self.tokenizer.token_type == Token.IDENTIFIER:
            self.write_identifier()
        self.tokenizer.advance()
        self.write_identifier()
        self.tokenizer.advance()
        while self.tokenizer.token == ",":
            self.write_symbol()
            self.tokenizer.advance()
            self.write_identifier()
            self.tokenizer.advance()
        self.write_symbol()
        self.tokenizer.advance()
