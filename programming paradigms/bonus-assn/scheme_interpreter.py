# Scheme interpreter written in python
# Author: Giorgi Kldiashvili

import math
from copy import copy
import sys
from random import randint
import re

# Regex to help tokenize raw string input
regex_str = '\(|\)|\"[^\"]*\"|\'[^\']*\'|[a-zA-Z][\w\?-]*|-?[0-9]+|-?[0-9]+.[0-9]+|<=|<|>=|>|=|\+|\*|-|/|#T|#t|#F|#f'

# Primitive Scheme functions
global_functions = {
    '+': sum,
    '*': math.prod,
    '-': lambda l: l[0] - sum(l[1:]),
    '/': lambda l: l[0] / l[1],
    'car': lambda l: l[0][0],
    'cdr': lambda l: l[0][1:],
    'length': lambda l: len(l[0]),
    'null?': lambda l: len(l[0]) == 0,
    'cons': lambda l: [l[0]] + l[1],
    'append': lambda l: [item for sublist in l for item in sublist],
    '=': lambda l: l.count(l[0]) == len(l) and l[0] == l[1],
    '<': lambda l: all(i < j for i, j in zip(l, l[1:])) and l[0] < l[1],
    '>': lambda l: all(i > j for i, j in zip(l, l[1:])) and l[0] > l[1],
    '<=': lambda l: all(i <= j for i, j in zip(l, l[1:])) and l[0] <= l[1],
    '>=': lambda l: all(i >= j for i, j in zip(l, l[1:])) and l[0] >= l[1],
    'and': lambda l: l[-1] if all(l) and len(l) > 0 else False if len(l) > 0 else True,
    'or': lambda l: False if len(l) == 0 else l[0] if len(l) == 1 else l[0] if l[0] else evaluate(['or', l[1:]])[0],
    'eval': lambda l: l[0],
    'display': lambda l: print(l[0]),
    'even?': lambda l: l[0] % 2 == 0,
    'odd?': lambda l: l[0] % 2 == 1,
    'zero?': lambda l: l[0] == 0,
    'exit': lambda l: exit(),
    'boolean?': lambda l: l[0] == True,
    'ceiling': lambda l: math.ceil(l[0]),
    'floor': lambda l: math.floor(l[0]),
    'cos': lambda l: math.cos(l[0]),
    'sin': lambda l: math.sin(l[0]),
    'empty?': lambda l: len(l[0]) == 0,
    'equal?': lambda l: l[0] == l[1],
    'expt': lambda l: l[0]**l[1],
    'integer?': lambda l: isinstance(l[0], int),
    'list': lambda l: [item for item in l],
    'list?': lambda l: isinstance(l[0], list),
    'max': lambda l: max(l),
    'min': lambda l: min(l),
    'not': lambda l: not l[0],
    'number?': lambda l: isinstance(l[0], int) or isinstance(l[0], float),
    'random': lambda l: randint(0, l[0] - 1),
    'round': lambda l: round(l[0]),
    'sqrt': lambda l: math.sqrt(l[0]),
    'square': lambda l: l[0]*l[0],
    'quotient': lambda l: l[0]//l[1],
    'remainder': lambda l: l[0] % l[1],
    'positive?': lambda l: l[0] > 0,
}

# Store which parameter names each new (define) function has (key - function name, value - function parameter names)
function_parameters = {}
# Store what function bodies are (key - function name, value - function body)
function_bodies = {}

# Replace parameter values into function body so that it can be evaluated
def deep_replace(func_name, func_body, func_param_values):
    tmp_func_body = copy(func_body)
    if len(function_parameters[func_name]) != len(func_param_values):
        print("Define function has incorrect number of parameters")
        exit()
    for i in range(len(func_body)):
        if isinstance(func_body[i], list):
            tmp_func_body[i] = deep_replace(func_name, func_body[i], func_param_values)
        else:
            if func_body[i] == func_name:
                continue
            for j in range(len(func_param_values)):
                if func_body[i] == function_parameters[func_name][j]:
                    tmp_func_body[i] = func_param_values[j]
    return tmp_func_body  

# Same as deep_replace but for Scheme lambda functions
def deep_replace_lambda(func_body, func_param_names, func_param_values):
    tmp_func_body = copy(func_body)
    if len(func_param_names) != len(func_param_values):
        print("Define function has incorrect number of parameters")
        exit()
    for i in range(len(func_body)):
        if isinstance(func_body[i], list):
            tmp_func_body[i] = deep_replace_lambda(func_body[i], func_param_names, func_param_values)
        else:
            for j in range(len(func_param_values)):
                if func_body[i] == func_param_names[j]:
                    tmp_func_body[i] = func_param_values[j]
    return tmp_func_body  

# Add new function to global_functions
def add_function(func_head, func_body):
    func_name = func_head[0]
    func_params = func_head[1:]
    function_parameters[func_name] = func_params
    function_bodies[func_name] = func_body
    func = lambda l: evaluate(deep_replace(func_name, func_body, l))
    global_functions[func_name] = func
    return


# Add new variable or function to global_functions
def add_expression(head, body):
    if isinstance(head, list):
        add_function(head,body)
        return None
    func = body
    if isinstance(func, str):
        func = func.replace('\'','').replace('\"','')
    global_functions[head] = func
    function_parameters[head] = None


# Turns standard scheme list into python list
def listify(tokens):
    token = tokens.pop(0)
    if token == '(':
        l = []
        while tokens[0] != ')':
            l.append(listify(tokens))
        tokens.pop(0)
        return l
    elif token == ')':
        print("Unexpected \')\' detected in scheme file")
        exit()
    else:
        try:
            return int(token)
        except:
            try:
                return float(token)
            except:
                if token in ['#T', '#t']:
                    return True
                if token in ['#F', '#f']:
                    return False
                return token


# Evaluates python list as Scheme list
def evaluate(l):
    if l is None:
        return
    if not isinstance(l, list):
        if l in function_parameters.keys() and function_parameters[l] is None:
            return global_functions[l]
        return l
    if len(l) == 0:
        return l
    if isinstance(l[0], list):
        if len(l[0]) > 1 and l[0][0] == 'lambda':
            return evaluate(deep_replace_lambda(l[0][2], l[0][1], l[1:]))
        return [evaluate(item) for item in l]
    if l[0] == 'apply':
        return evaluate([l[1]] + l[2])
    if l[0] == 'map':
        return [evaluate([l[1], item]) for item in evaluate(l[2])]
    if l[0] == 'define':
        add_expression(l[1],l[2])
        return
    if l[0] == 'if':
        if evaluate(l[1]):
            return evaluate(l[2])
        elif len(l) == 4:
            return evaluate(l[3])
        else:
            return None

    func_key = l[0]
    if func_key not in global_functions.keys() or (func_key in function_parameters.keys() and function_parameters[func_key] is None):
        return [evaluate(item) for item in l]
    func = global_functions[func_key]
    
    return func(evaluate(l[1:]))
    


# Turns raw tokenized string into multiple queries and returns list of those queries
def turn_list_to_queries(tokenized_string):
    queries = []
    query = []
    count = 0
    for i in range(len(tokenized_string)):
        if (tokenized_string[i] == '('):
            count = count + 1
        elif (tokenized_string[i] == ')'):
            count = count - 1
        query.append(tokenized_string[i])
        if count == 0:
            queries.append(listify(query))
            query = []
    return queries


#
#
# Main body of interpreter
#
#
# Is called when live interpreter uses (load) or when script is called from console with additional file
def main(file_name: str):
    # Opens file and puts out an exception if script can't access file
    file_name = file_name.replace('\'','')
    try:
        file = open(file_name, "r")
    except IOError:
        print("It seems like that file doesn't exist")
        exit()

    # Tokenize input file
    file_string = file.read().replace('(',' ( ').replace(')', ' ) ').replace('\' (', '(')
    tokenized_string = re.findall(regex_str, file_string)
    
    # If scheme file has multiple queries then all of them will be put into this list
    queries = turn_list_to_queries(tokenized_string)
    for query in queries:
        evaluate(query)
    
    file.close()

# Is called from live interpreter function to run queries
def interpret(s):
     # Tokenize input
    parsed = s.replace('(',' ( ').replace(')', ' ) ').replace('\' (', '(')
    tokenized_string = re.findall(regex_str, parsed)
    
    if tokenized_string[1] == 'load':
        main(tokenized_string[2])
        return

    # If scheme file has multiple queries then all of them will be put into this list
    queries = turn_list_to_queries(tokenized_string)
    for query in queries:
        result = evaluate(query)
        if result is not None:
            print(result)


# Handles input from console for live interpretation
if __name__ == '__main__':
    if len(sys.argv) != 2:
        i = 0
        brackets = 0
        p_inp = ""
        while True:
            inp = input(str(i) + '#scm>>')
            p_inp = p_inp + inp
            brackets = brackets + inp.count('(') - inp.count(')')
            i = i + 1
            if inp == '(exit)':
                break
            elif inp == 'exit':
                print('Did you mean \'(exit)\'?')
            elif brackets == 0 and len(p_inp) != 0:
                interpret(p_inp)
                p_inp = ""
            
    else:
        main(sys.argv[1])

