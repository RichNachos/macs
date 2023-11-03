import re
from typing import Iterable

OP_MAP = {
    "<": "&lt;",
    ">": "&gt;",
    "&": "&amp;",
}

KEYWORD_REGEX = re.compile(
    r"^\s*("
    r"class|constructor|function|method|static|field"
    r"|var|int|char|boolean|void|true|false|null|this|"
    r"let|if|else|do|while|return)\s*"
)
SYMBOL_REGEX = re.compile(r"^\s*([{}()\[\].,;~&|<=>+\-*/])\s*")
DIGIT_REGEX = re.compile(r"^\s*(\d+)\s*")
STRING_REGEX = re.compile(r'^\s*"(.*)"\s*')
IDENTIFIER_REGEX = re.compile(r"^\s*([a-zA-Z1-9_][a-zA-Z_]*)\s*")


class Token:
    KEYWORD = "keyword"
    SYMBOL = "symbol"
    IDENTIFIER = "identifier"
    INT_CONST = "integerConstant"
    STR_CONST = "stringConstant"


class JackTokenizer:
    def __init__(self, inp: Iterable[str]) -> None:
        self.inp: str = "{}".format("\n".join(list(inp)[0:]))
        self.inp = re.sub(
            r"(/\*([\r\n]|(\*+([^*/]|[\r\n]))|[^*])*\*+/)|(//.*)", "", self.inp
        )
        self.token_type: str = ""
        self.token: str = ""

    def has_more_tokens(self) -> bool:
        if re.fullmatch(re.compile(r"\s*"), self.inp):
            return False
        return True

    def advance(self) -> None:
        if self.has_more_tokens():
            self.match()

    def match(self) -> None:
        match = re.match(KEYWORD_REGEX, self.inp)
        if match is not None:
            self.inp = re.sub(KEYWORD_REGEX, "", self.inp)
            self.token_type = Token.KEYWORD
            self.token = match.group(1)
            return
        match = re.match(SYMBOL_REGEX, self.inp)
        if match is not None:
            self.inp = re.sub(SYMBOL_REGEX, "", self.inp)
            self.token_type = Token.SYMBOL
            self.token = match.group(1)
            if self.token in OP_MAP.keys():
                self.token = OP_MAP[self.token]
            return
        match = re.match(DIGIT_REGEX, self.inp)
        if match is not None:
            self.inp = re.sub(DIGIT_REGEX, "", self.inp)
            self.token_type = Token.INT_CONST
            self.token = match.group(1)
            return
        match = re.match(STRING_REGEX, self.inp)
        if match is not None:
            self.inp = re.sub(STRING_REGEX, "", self.inp)
            self.token_type = Token.STR_CONST
            self.token = match.group(1)
            return
        match = re.match(IDENTIFIER_REGEX, self.inp)
        if match is not None:
            self.inp = re.sub(IDENTIFIER_REGEX, "", self.inp)
            self.token_type = Token.IDENTIFIER
            self.token = match.group(1)
            return
