# Отчёт о проведённом тестировании

## 1. Краткое описание
*"Проведено тестирование платежной формы веб-приложения. Проверены валидация полей, обработка ошибок и успешные сценарии оплаты."*

## 2. Тест-кейсы
- **Общее количество тест-кейсов:** 56
- **Успешные:** 37 (66.07% успешных)
- **Неуспешные:** 19 (33,92% неуспешных)


## 3. Баги
- [Issue #1](https://github.com/lewkAa/AQA-Diploma/issues/1#issue-3080797538 )
- [Issue #2](https://github.com/lewkAa/AQA-Diploma/issues/2#issue-3080837411)
- [Issue #3](https://github.com/lewkAa/AQA-Diploma/issues/3#issue-3080891638)
- [Issue #4](https://github.com/lewkAa/AQA-Diploma/issues/4#issue-3096625677)
- [Issue #5](https://github.com/lewkAa/AQA-Diploma/issues/5#issue-3096647593)
- [Issue #6](https://github.com/lewkAa/AQA-Diploma/issues/6#issue-3096658444)
- [Issue #7](https://github.com/lewkAa/AQA-Diploma/issues/7#issue-3096666176)
- [Issue #8](https://github.com/lewkAa/AQA-Diploma/issues/8#issue-3096758023)
- [Issue #9](https://github.com/lewkAa/AQA-Diploma/issues/9#issue-3096778538)
- [Issue #10](https://github.com/lewkAa/AQA-Diploma/issues/10#issue-3096800480)
- [Issue #11](https://github.com/lewkAa/AQA-Diploma/issues/11#issue-3096841993)
- [Issue #12](https://github.com/lewkAa/AQA-Diploma/issues/12#issue-3096901639)

### Критические проблемы
- **Валидация поля "Владелец"**  
  Необходимо срочно добавить валидацию на стороне frontend и backend ([Issue #1](https://github.com/lewkAa/AQA-Diploma/issues/1#issue-3080797538)).  
  **Риски:**
    - Некорректные данные попадают в БД.
    - Потенциальная уязвимость для SQL-инъекций или XSS-атак.

### Улучшения
- **Тестовые метки**  
  Внедрить уникальные `data-testid` (или аналоги) для ключевых элементов UI.  
  **Преимущества:**
    - Ускорение написания и поддержки UI-тестов.
    - Повышение стабильности тестов при изменениях вёрстки.
    - Снижение зависимости от CSS-селекторов.



---

## Интеграция с системами отчётности
Приложены автоматизированные отчёты:

- [Allure Report](https://github.com/user-attachments/assets/6dcb322c-bf2b-467c-b67d-ef665813d37f)
