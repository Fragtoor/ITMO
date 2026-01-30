# Author = Sivkov Alexander Vladimirovich
# Group = P3116
# Date = 09.10.2025


from re import *
from string import ascii_letters


def check_passw(passw: str) -> dict:
    errors = {
    }
    if len(passw) < 5:
        errors['Rule_1'] = 'Your password must be at least 5 characters'
    pattern = {
        'digit': bool(search(r'\d', passw)),
        'uppercase': bool(search(r'[A-Z]', passw)),
        'sum_digit': sum(int(d) for d in findall(r'\d', passw)) == 25,
        'month': bool(search(r'january|february|march|april|may|june|july|august|september|october|november|december', passw, IGNORECASE)),
        'spec_symbol': bool(search(r'[!@#$%^&*()_+\-=\[\]{}|;:",.<>/?`~]', passw, IGNORECASE))
    }
    if not pattern['digit']:
        errors['Rule_2'] = 'Your password must include a number'
    if not pattern['uppercase']:
        errors['Rule_3'] = 'Your password must include an uppercase letter'
    if not pattern['spec_symbol']:
        errors['Rule_4'] = 'Your password must include a special character'
    if not pattern['sum_digit']:
        errors['Rule_5'] = 'The digits in your password must add up 25'
    if not pattern['month']:
        errors['Rule_6'] = 'Your password must include a month of the year'
    return errors
    

def run_test():
    tests = [
        {
            "input": "January!889Password",
            "expected": {}
        },
        {
            "input": "Ab1!",
            "expected": {
                'Rule_1': 'Your password must be at least 5 characters',
                'Rule_5': 'The digits in your password must add up 25',
                'Rule_6': 'Your password must include a month of the year'
            }
        },
        {
            "input": "March2025OpenAI",
            "expected": {
                'Rule_4': 'Your password must include a special character',
                'Rule_5': 'The digits in your password must add up 25',
            }
        },
        {
            "input": "May-889.Secure",
            "expected": {}
        },
        {
            "input": "febru997!lockx",
            "expected": {
                'Rule_3': 'Your password must include an uppercase letter',
                'Rule_6': 'Your password must include a month of the year'
            }
        },
        
    ]
    for test in tests:
        res = check_passw(test['input'])
        if res != test['expected']:
            return test['input']
    return True



if __name__ == '__main__':
    res_test = run_test()
    if res_test is True:
        print('Все тесты прошли успешно')
    else:
        print(f'Программа выдаёт неверный результат для пароля: {res_test}')
