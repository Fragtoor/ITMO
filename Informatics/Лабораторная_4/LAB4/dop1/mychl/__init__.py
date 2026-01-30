
def dump(obj):
    main_list = []
    to_string(obj, main_list, 0)
    return '\n'.join(main_list)
    

def to_string(obj, main_list, indent):     
    if not obj:
        return
    
    keys = []
    if isinstance(obj, dict):
        keys = list(obj.keys())
        is_list = False
    else:
        is_list = True
    
    if not is_list:
        for i, key in enumerate(keys):
            if (not isinstance(obj[key], list)) and (not isinstance(obj[key], dict)):
                if isinstance(obj[key], str):
                    main_list += [' ' * indent + key + ' = "' + str(obj[key]) + '"']
                else:
                    main_list += [' ' * indent + key + ' = ' + str(obj[key])]
                continue

            if isinstance(obj[key], list):
                main_list += [' ' * indent + key + ' = [']
                to_string(obj[key], main_list, indent + 2)
                main_list += [' ' * indent + ']']
            elif isinstance(obj[key], dict):
                main_list += [' ' * indent + key + ' = {']
                to_string(obj[key], main_list, indent + 2)
                main_list += [' ' * indent + '}']
            else:
                if isinstance(obj[key], str):
                    main_list += [' ' * indent + key + ' = "' + str(obj[key]) + '"']
                else:
                    main_list += [' ' * indent + key + ' = ' + str(obj[key])]

    else:
        for i, elem in enumerate(obj):
            if isinstance(elem, list):
                main_list += [' ' * indent + '[']
                to_string(elem, main_list, indent + 2)
                main_list += [' ' * indent + ']' + ',' * (1 if i < len(obj) - 1 else 0)]
            elif isinstance(elem, dict):
                main_list += [' ' * indent + '{']
                to_string(elem, main_list, indent + 2)
                main_list += [' ' * indent + '}' + ',' * (1 if i < len(obj) - 1 else 0)]
            else:
                if isinstance(elem, str):
                    main_list += [' ' * indent + '"' + str(elem) + '"' + ',' * (1 if i < len(obj) - 1 else 0)]
                else:
                    main_list += [' ' * indent + str(elem) + ',' * (1 if i < len(obj) - 1 else 0)]
