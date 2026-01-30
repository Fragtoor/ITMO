
def loads(string):
    return parse_yaml(string)


def _parse_value(value):
    """Парсит скалярное значение"""
    value = value.strip()
    if (value.startswith('"') and value.endswith('"')) or (value.startswith("'") and value.endswith("'")):
        return value[1:-1]
    if value == 'true':
        return True
    if value == 'false':
        return False
    if value == 'null':
        return None
    try:
        return int(value)
    except ValueError:
        try:
            return float(value)
        except ValueError:
            # Случаи списков и словарей
            if '[' in value:
                value = value[1:-1].split(', ')
                result_list = [_parse_value(elem) for elem in value]
                return result_list

            elif '{' in value:
                value = value[1:-1].split(', ')
                result_dict = {}
                for elem in value:
                    # elem вида строки key: value
                    print(elem)
                    key, val = elem.split(':', 1)
                    result_dict[_parse_value(key)] = _parse_value(val)
                return result_dict
            else:
                return value


def parse_yaml(yaml_string):
    """Парсер YAML"""

    lines = yaml_string.strip().split('\n')
    if not lines:
        return None

    # Стек для хранения (уровень_отступа, контейнер)
    stack = [( -1, {} )]
    # Результат работы программы(корень)
    root = stack[0][1]

    for i, line in enumerate(lines):
        if not line.strip() or line.strip().startswith('#'):
            continue

        indent = len(line) - len(line.lstrip(' '))
        stripped_line = line.strip()

        # Выход из вложенных структур(закончили заполнять крайний контейнер)
        while indent <= stack[-1][0]:
            stack.pop()

        # С этого момента контейнер верхнего элемента стэка - родительский(куда мы должны добавить текущий элемент)
        parent_indent, parent_container = stack[-1]

        # Анализ строки
        if stripped_line.startswith('- '):
            # Элемент списка
            if not isinstance(parent_container, list):
                raise ValueError(f"Ошибка: элемент списка '{stripped_line}' не может находиться внутри словаря. Строка: '{line}'")

            item_content = stripped_line[2:].strip()

            if ':' in item_content:
                # Элемент списка является словарем
                key_str, value_str = item_content.split(':', 1)
                key = _parse_value(key_str)

                # Создаем словарь для этого элемента списка
                item_dict = {}
                parent_container.append(item_dict)

                if value_str.strip():
                    # Случай "- key: value"
                    item_dict[key] = _parse_value(value_str)
                    # Этот словарь становится новым контекстом для следующих ключей на том же уровне
                    stack.append((indent, item_dict))
                else:
                    # Случай "- key:", за которым следует блок
                    # Определяем, будет блок списком или словарем
                    is_next_list = False
                    if i + 1 < len(lines):
                        next_line = lines[i+1]
                        next_indent = len(next_line) - len(next_line.lstrip(' '))
                        if next_indent > indent and next_line.strip().startswith('-'):
                            is_next_list = True

                    new_container = [] if is_next_list else {}
                    item_dict[key] = new_container
                    # Новый контекст - это созданный контейнер (список или словарь)
                    stack.append((indent, new_container))
            else:
                # Простой элемент списка
                parent_container.append(_parse_value(item_content))

        elif ':' in stripped_line:
            # Пара ключ-значение
            if not isinstance(parent_container, dict):
                raise ValueError(f"Ошибка в синтаксисе: ключ-значение в списке. Строка: '{line}'")

            key_str, value_str = stripped_line.split(':', 1)
            key = _parse_value(key_str)

            if value_str.strip():
                # Простой случай "key: value"
                parent_container[key] = _parse_value(value_str)
            else:
                # Случай "key:", за которым следует блок
                is_next_list = False
                if i + 1 < len(lines):
                    next_line = lines[i+1]
                    next_indent = len(next_line) - len(next_line.lstrip(' '))
                    if next_indent > indent and next_line.strip().startswith('-'):
                        is_next_list = True

                new_container = [] if is_next_list else {}
                parent_container[key] = new_container
                stack.append((indent, new_container))

    return root
