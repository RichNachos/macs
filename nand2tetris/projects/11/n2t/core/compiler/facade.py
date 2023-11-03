from __future__ import annotations

from dataclasses import dataclass
from typing import Iterable, List

from n2t.core.compiler.compilation_engine import CompilationEngine
from n2t.core.compiler.jack_tokenizer import JackTokenizer


@dataclass
class Compiler:
    @classmethod
    def create(cls) -> Compiler:
        return cls()

    def compile(self, jack: Iterable[str]) -> List[str]:
        engine = CompilationEngine(jack)
        xml = engine.compile()
        return xml

    def tokenize(self, jack: Iterable[str]) -> List[str]:
        tokens_xml: List[str] = ["<tokens>"]
        tokenizer = JackTokenizer(jack)
        while tokenizer.has_more_tokens():
            tokenizer.advance()
            token_type = tokenizer.token_type
            token = tokenizer.token
            tokens_xml.append(f"<{token_type.__str__()}> {token} </{token_type}>")

        tokens_xml.append("</tokens>")
        return tokens_xml
