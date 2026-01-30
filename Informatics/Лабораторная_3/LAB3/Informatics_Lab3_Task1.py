# Author = Sivkov Alexander Vladimirovich
# Group = P3116
# Date = 09.10.2025


from re import *


def regex(text: str) -> list:
    vowel = 'аеёиоуыэюяaeiou'
    consonant = 'бвгджзйклмнпрстфхцчшщbcdfghjklmnpqrstvwxyz'
    cnt = '{0,1}'
    words = text.split()
    pattern1 = fr'[a-zа-я]*[{vowel}][{vowel}][a-zа-я]*'
    words2 = []
    result = []
    for word in words:
        if fullmatch(pattern1, word, IGNORECASE):
            words2.append(word)
    for word in words2:
        pattern2 = fr'[{vowel}]*[{consonant}]{cnt}[{vowel}]*[{consonant}]{cnt}[{vowel}]*[{consonant}]{cnt}[{vowel}]*'
        if words.index(word) < len(words) - 1:
            if fullmatch(pattern2, words[words.index(word) + 1], IGNORECASE):
                result.append(word)
    return result


def run_test():
    tests = [
        {
            "input": "Кривошеее существо гуляет по парку с друзьями",
            "expected": ["гуляет"]
        },
        {
            "input": "Hello school meet my good old friend here", 
            "expected": ["school", "meet", "good", 'friend']
        },
        {
            "input": "Python програММирование coffee книга paper тетрадь",
            "expected": ["програММирование", "coffee"]
        },
        {
            "input": "зеленое дерево стоит у высокой горы в лесу",
            "expected": ["зеленое", "стоит"] 
        },
        {
            "input": "биология химия физика математика география история литература", 
            "expected": ["биология", 'химия', "география"]
        },
        {
            "input": "АИст летАет высоко орел парит в небе ястреб ищет добычу",
            "expected": ["АИст", "летАет",]
        }
    ]
    for test in tests:
        res = regex(test['input'])
        if res != test['expected']:
            return test['input']
    return True



if __name__ == '__main__':
    res_test = run_test()
    if res_test is True:
        print('Все тесты прошли успешно')
    else:
        print(f'Программа выдаёт неверный результат для текста: {res_test}')
