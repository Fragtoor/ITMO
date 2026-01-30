code = input('Введите набор 7 цифр: ')
information_cat = [int(code[2]), int(code[4]), int(code[5]), int(code[6])]
control_cat = [int(code[0]), int(code[1]), int(code[3])]
s1 = control_cat[0] ^ information_cat[0] ^ information_cat[1] ^ information_cat[3]
s2 = control_cat[1] ^ information_cat[0] ^ information_cat[2] ^ information_cat[3]
s3 = control_cat[2] ^ information_cat[1] ^ information_cat[2] ^ information_cat[3]
s = str(s3) + str(s2) + str(s1)
if s == '000':
    print('Код верный')
else:
    print(f'Ошибка в бите в разряде под номером {int(s, 2)}')
    code2 = list(code)
    code2[int(s, 2) - 1] = '0' if code2[int(s, 2) - 1] == '1' else '1'
    code2[0] = ''
    code2[1] = ''
    code2[3] = ''
    print(f'Правильное сообщение: {''.join(code2)}')
