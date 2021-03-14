# StoriesView - Compoenente de Vista 

![example2](https://user-images.githubusercontent.com/28780954/111054899-0bc9f600-844f-11eb-8ada-7c1f21772622.gif)

## Descripción

Se trata de una test app y un módulo contenedor con tres componentes de vistas: `PausableProgressBar`, `StoriesProgressView`y `StoriesView`.

## Implementación 

### Layout

```
    <com.example.stories_libary.StoriesView
        android:id="@+id/myStoriesView"
        android:layout_width="0dp"
        android:layout_height="0dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"/>

```


### Activity contenedora

Se requiere la implementacion de los metodos `override fun loadView(position: Int)` y  `override fun onComplete()` de `StoriesView.Listener` y setearlo a `StoriesView`.

```
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stories_view)
        mImageView = findViewById(R.id.imageView)
        setStoriesView()
    }

    private fun setStoriesView() {
        storiesView = findViewById(R.id.myStoriesView)
        with(storiesView) {
            setListener(this@StoriesViewActivity)
            startAnimationWithSize(listSize = resourcesList.size, frequencyRange = 50)
        }
    }

    override fun loadView(position: Int) {
        resourcesList.getOrNull(position)?.let {
            mImageView.setImageDrawable(getDrawable(it))
        }
    }

    override fun onComplete() {
        onBackPressed()
    }
```
