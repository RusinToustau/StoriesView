# StoriesView - Compoenente de Vista 

La idea es mostrar un walkthrough con el formato de stories de Instagram. 
Este primer proyecto contiene tres componentes para lograr tanto la animación como los eventos de skip, reverse y pause que tienen las stories. 


![example2](https://user-images.githubusercontent.com/28780954/111054899-0bc9f600-844f-11eb-8ada-7c1f21772622.gif)

| Componente | Responsabilidad |
| ------ | ------ |
| `PausableProgressBar` | Un progressbar custom que permite ser pausado al que se le puede setear la duración(en milisegundos)  |
| `StoriesProgressView` | Es un Linear layout con un conjunto de `PausableProgressBar` separadas por un espacio. Permite frenar el proceso en cualquier instancia | 
| `StoriesView` | Es una vista contenedora, que tiene en conjunto el StoriesProgressView (con N `PausableProgressBar`) y dos `View` areas que tienen asignadas listeners motion event para pausar, avanzar o retroceder  |

| **Distribución de la vista** |
| ------ |
| <img width="694" alt="Captura_de_Pantalla_2021-05-12_a_la_s__16 18 52" src="https://user-images.githubusercontent.com/28780954/121787831-df815b00-cb9e-11eb-8332-3f43f8d17401.png"> |

## Implementación 


````
class StoriesViewActivity : AppCompatActivity(), StoriesView.Listener {
    private lateinit var storiesView: StoriesView

    override fun onCreate(savedInstanceState: Bundle?) {
    ....
        setStoriesView()
    }

    private fun setStoriesView() {
        storiesView = findViewById(R.id.myStoriesView)
        with(storiesView) {
            setListener(this@StoriesViewActivity)
            startAnimationWithSize(listSize = resourcesList.size, duration = 5000)
        }
    }


   // Sobrescribir  onDestroy(), onPause(), onResume()

    override fun onDestroy() {
        storiesView.destroy()
        super.onDestroy()
    }

    override fun onPause() {
        storiesView.onPause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        storiesView.onResume()
    }
}

```` 

La implementación de StoriesView.Listener será a travez de `onPositionChanged(position: Int)` y `override fun onComplete()`. 

**onPositionChanged**

Se disparará cada vez que haya un cambio de posición, ya sea por finalización del tiempo de duración o por un evento de onClick u onTouch (cuando el usuario avanza, retrocede), indicando el index correspondiente a la animación que este reproduciéndose. 

**onComplete**

Se disparará cuando finalice de reproducirse la última animación.

* en estos caso llamamos animación al componente `PausableProgressBar

````

    override fun onPositionChanged(position: Int) {
        resourcesList.getOrNull(position)?.let {
            mImageView.setImageDrawable(getDrawable(it))
        }
    }

    override fun onComplete() {
        onBackPressed()
    }

````
