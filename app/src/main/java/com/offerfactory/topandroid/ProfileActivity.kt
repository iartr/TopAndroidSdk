package com.offerfactory.topandroid

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

/**
 * Activity для редактирования профиля пользователя.
 * 
 * ДОМАШНЕЕ ЗАДАНИЕ:
 * 
 * Реализуйте полнофункциональный экран профиля со следующими требованиями:
 * 
 * 1. ПОЛЯ ФОРМЫ:
 *    - Имя (EditText): валидация 2-40 символов
 *    - Возраст (EditText, inputType=number): валидация 10-120
 *    - Любимый жанр (Spinner): не должен быть "Выберите жанр"
 *    - О себе (EditText, multiline): необязательное поле
 * 
 * 2. ВАЛИДАЦИЯ:
 *    - Показывать inline-ошибки через EditText.error
 *    - Кнопка "Сохранить" активна только когда все поля валидны
 *    - Использовать TextWatcher для отслеживания изменений
 * 
 * 3. СОХРАНЕНИЕ ДАННЫХ:
 *    - Создайте data class UserProfile(name, age, genre, about)
 *    - Создайте object ProfileRepository с var currentProfile: UserProfile?
 *    - При нажатии "Сохранить" сохраняйте в ProfileRepository
 * 
 * 4. НАВИГАЦИЯ:
 *    - При сохранении: показать Toast "Профиль сохранён"
 *    - Вернуться на MovieDetailActivity через setResult() и finish()
 *    - Передать имя через Intent extra с ключом "saved_name"
 *    - При "Отмена" просто вызвать finish()
 * 
 * 5. ПОВОРОТ ЭКРАНА:
 *    - Сохранять все несохранённые данные формы в onSaveInstanceState()
 *    - Восстанавливать в onCreate()
 * 
 * 6. ВЕРСИЯ ПРИЛОЖЕНИЯ:
 *    - Показать TextView внизу с текстом "Версия приложения: X.X"
 *    - Использовать BuildConfig.VERSION_NAME
 * 
 * 7. TOOLBAR MENU (в MovieDetailActivity):
 *    - Добавить пункт меню "Профиль"
 *    - При клике открывать ProfileActivity
 *    - Использовать ActivityResultLauncher для получения результата
 *    - Если получено имя, показать "Привет, [Имя]!" в MovieDetailActivity
 */
class ProfileActivity : AppCompatActivity() {
    
    companion object {
        private const val TAG = "ProfileActivity"
        
        // TODO: Добавьте константы для ключей Bundle
        // private const val KEY_NAME = "name"
        // private const val KEY_AGE = "age"
        // private const val KEY_GENRE = "genre"
        // private const val KEY_ABOUT = "about"
        
        // Ключ для передачи имени обратно в MovieDetailActivity
        const val EXTRA_SAVED_NAME = "saved_name"
    }
    
    // TODO: Объявите UI элементы
    // private lateinit var greetingTextView: TextView
    // private lateinit var nameEditText: EditText
    // private lateinit var ageEditText: EditText
    // private lateinit var genreSpinner: Spinner
    // private lateinit var genreErrorTextView: TextView
    // private lateinit var aboutEditText: EditText
    // private lateinit var versionTextView: TextView
    // private lateinit var saveButton: Button
    // private lateinit var cancelButton: Button
    
    // TODO: Переменные для хранения данных формы
    // private var isNameValid = false
    // private var isAgeValid = false
    // private var isGenreValid = false
    
    /**
     * TODO: Реализуйте onCreate()
     * 
     * 1. Вызовите super.onCreate()
     * 2. Логируйте вызов метода
     * 3. Установите layout (setContentView)
     * 4. Инициализируйте UI элементы через findViewById
     * 5. Восстановите состояние из savedInstanceState
     * 6. Загрузите сохранённый профиль из ProfileRepository (если есть)
     * 7. Настройте Spinner с массивом жанров
     * 8. Настройте TextWatcher для полей ввода
     * 9. Настройте обработчики кликов для кнопок
     * 10. Установите версию приложения
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate called")
        
        // TODO: Ваш код здесь
    }
    
    // TODO: Добавьте методы жизненного цикла с логированием
    // override fun onStart() { ... }
    // override fun onResume() { ... }
    // override fun onPause() { ... }
    // override fun onStop() { ... }
    // override fun onDestroy() { ... }
    
    /**
     * TODO: Реализуйте onSaveInstanceState()
     * 
     * Сохраните текущие значения полей формы в Bundle,
     * чтобы они не терялись при повороте экрана.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "onSaveInstanceState called")
        
        // TODO: Сохраните значения полей
        // outState.putString(KEY_NAME, nameEditText.text.toString())
        // ...
    }
    
    /**
     * TODO: Создайте функцию валидации имени
     * 
     * @return true если имя от 2 до 40 символов
     */
    private fun validateName(name: String): Boolean {
        // TODO: Реализуйте валидацию
        return false
    }
    
    /**
     * TODO: Создайте функцию валидации возраста
     * 
     * @return true если возраст от 10 до 120
     */
    private fun validateAge(ageText: String): Boolean {
        // TODO: Реализуйте валидацию
        // Подсказка: используйте toIntOrNull()
        return false
    }
    
    /**
     * TODO: Создайте функцию для обновления состояния кнопки "Сохранить"
     * 
     * Кнопка должна быть активна только когда все поля валидны.
     */
    private fun updateSaveButtonState() {
        // TODO: Реализуйте
        // saveButton.isEnabled = isNameValid && isAgeValid && isGenreValid
    }
    
    /**
     * TODO: Создайте функцию настройки TextWatcher для полей
     * 
     * Подсказка:
     * nameEditText.addTextChangedListener(object : TextWatcher {
     *     override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
     *     override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
     *     
     *     override fun afterTextChanged(s: Editable?) {
     *         val isValid = validateName(s.toString())
     *         nameEditText.error = if (!isValid) getString(R.string.name_error) else null
     *         isNameValid = isValid
     *         updateSaveButtonState()
     *     }
     * })
     */
    private fun setupTextWatchers() {
        // TODO: Настройте TextWatcher для nameEditText
        // TODO: Настройте TextWatcher для ageEditText
    }
    
    /**
     * TODO: Создайте функцию настройки Spinner
     * 
     * Подсказка:
     * val adapter = ArrayAdapter.createFromResource(
     *     this,
     *     R.array.genres,
     *     android.R.layout.simple_spinner_item
     * )
     * adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
     * genreSpinner.adapter = adapter
     * 
     * genreSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
     *     override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
     *         isGenreValid = position != 0  // Не "Выберите жанр"
     *         genreErrorTextView.visibility = if (isGenreValid) View.GONE else View.VISIBLE
     *         updateSaveButtonState()
     *     }
     *     
     *     override fun onNothingSelected(parent: AdapterView<*>?) {}
     * }
     */
    private fun setupGenreSpinner() {
        // TODO: Реализуйте
    }
    
    /**
     * TODO: Создайте функцию настройки обработчиков кликов
     * 
     * saveButton.setOnClickListener {
     *     // 1. Создать UserProfile с данными из полей
     *     // 2. Сохранить в ProfileRepository.currentProfile
     *     // 3. Показать Toast "Профиль сохранён"
     *     // 4. Создать Intent с именем в extras
     *     // 5. Вызвать setResult(RESULT_OK, intent)
     *     // 6. Вызвать finish()
     * }
     * 
     * cancelButton.setOnClickListener {
     *     finish()
     * }
     */
    private fun setupClickListeners() {
        // TODO: Реализуйте
    }
}

/**
 * TODO: Создайте data class для профиля пользователя
 * 
 * data class UserProfile(
 *     val name: String,
 *     val age: Int,
 *     val genre: String,
 *     val about: String
 * )
 */

/**
 * TODO: Создайте синглтон для хранения профиля
 * 
 * object ProfileRepository {
 *     var currentProfile: UserProfile? = null
 * }
 */

/**
 * ЧЕКЛИСТ ДЛЯ ПРОВЕРКИ:
 * 
 * [ ] ProfileActivity создана и зарегистрирована в AndroidManifest.xml
 * [ ] Layout с всеми полями создан (используя LinearLayout/FrameLayout)
 * [ ] Валидация имени работает (2-40 символов)
 * [ ] Валидация возраста работает (10-120)
 * [ ] Валидация жанра работает (не "Выберите жанр")
 * [ ] Inline-ошибки показываются через EditText.error
 * [ ] Кнопка "Сохранить" активна только при валидных данных
 * [ ] TextWatcher настроены для автоматической валидации
 * [ ] Spinner с жанрами работает
 * [ ] При сохранении данные записываются в ProfileRepository
 * [ ] Toast "Профиль сохранён" показывается
 * [ ] Имя передаётся обратно через Intent extra
 * [ ] Состояние формы сохраняется при повороте (onSaveInstanceState)
 * [ ] Версия приложения отображается (BuildConfig.VERSION_NAME)
 * [ ] Все lifecycle методы логируются
 * [ ] Toolbar menu добавлено в MovieDetailActivity
 * [ ] ProfileActivity открывается через ActivityResultLauncher
 * [ ] В MovieDetailActivity отображается "Привет, [Имя]!"
 * [ ] Строки вынесены в strings.xml (нет hardcode)
 */
