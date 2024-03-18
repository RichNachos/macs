pub fn is_all_caps(message: &str) -> bool {
    let mut has_alphabetic = false;
    for c in message.chars() {
        if !c.is_ascii_uppercase() && c.is_alphabetic() {
            return false;
        }
        if c.is_alphabetic() {
            has_alphabetic = true;
        }
    }
    if !has_alphabetic {
        return false
    }
    true
}

pub fn is_question(message: &str) -> bool {
    if let Some(last) = message.chars().last() {
        return last == '?';
    }
    false
}

pub fn is_alphabetic(message: &str) -> bool {
    for c in message.chars() {
        if c.is_alphabetic() {
            return true
        }
    }
    false
}

pub fn reply(message: &str) -> &str {
    let message = message.trim();
    if message.is_empty() {
        return "Fine. Be that way!"
    }
    match (is_all_caps(message), is_question(message)) {
        (false, false) => {
            return "Whatever."
        }
        (false, true) => {
            return "Sure."
        }
        (true, false) => {
            return "Whoa, chill out!"
        }
        (true, true) => {
            return "Calm down, I know what I'm doing!"
        }
    }

}
