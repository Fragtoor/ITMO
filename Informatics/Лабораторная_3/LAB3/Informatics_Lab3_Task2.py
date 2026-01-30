# Author = Sivkov Alexander Vladimirovich
# Group = P3116
# Date = 09.10.2025


from re import *
from string import ascii_letters


def check_email(email: str) -> str:
    cnt = '{0,1}'
    pattern = fr'[a-zа-я0-9_.]+@[a-zа-я.]+[.][a-zа-я]+'
    if fullmatch(pattern, email, IGNORECASE):
        return email[email.index('@')+1:]
    return 'Fail!'


def run_test():
    tests = [
        {
            "input": "..@y.ru",
            "expected": "y.ru"
        },
        {
            "input": "students.spam@yandex.ru",
            "expected": "yandex.ru"
        },
        {
            "input": "example@example",
            "expected": "Fail!"
        },
        {
            "input": "example@example.com",
            "expected": "example.com"
        },
        {
            "input": "user@server.com",
            "expected": "server.com"
        },
        {
            "input": "user-name@domain.org",
            "expected": "Fail!"
        },
        {
            "input": "user@sub.domain.com",
            "expected": "sub.domain.com"
        },
        {
            "input": "user@123.com", 
            "expected": "Fail!"
        },
        {
            "input": "user@domain.co.uk",
            "expected": "domain.co.uk"
        },
        {
            "input": "_user@mail.ru",
            "expected": "mail.ru"
        },
        {
            "input": "user.@mail.ru",
            "expected": "mail.ru"
        },
        {
            "input": ".user@mail.ru",
            "expected": "mail.ru"
        },
        {
            "input": "user..name@mail.ru",
            "expected": "mail.ru"
        },
        {
            "input": "USER@MAIL.RU",
            "expected": "MAIL.RU"
        }
    ]
    for test in tests:
        res = check_email(test['input'])
        if res != test['expected']:
            return test['input']
    return True



if __name__ == '__main__':
    res_test = run_test()
    if res_test is True:
        print('Все тесты прошли успешно')
    else:
        print(f'Программа выдаёт неверный результат для текста: {res_test}')
