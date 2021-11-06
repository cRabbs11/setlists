package com.example.evgeny.setlist_mobile.setlistsSearch


import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import com.example.evgeny.setlist_mobile.mvp.PresenterBase
import com.example.evgeny.setlist_mobile.setlists.Setlist
import com.example.evgeny.setlist_mobile.utils.AnswerListener
import com.example.evgeny.setlist_mobile.utils.SetlistsRepository


public class SetlistsSearchPresenter(setlistsRepository: SetlistsRepository):
        PresenterBase<SetlistsSearchContract.View>(), SetlistsSearchContract.Presenter {

	private val TAG = SetlistsSearchPresenter::class.java.name + " BMTH "
	private val setlistsRepository: SetlistsRepository
	private var setlistsPage: Int = 1

	override fun onListItemClicked(setlist: Setlist) {
		setlistsRepository.setCurrentSetlist(setlist)
		getView()?.openSetlist()
	}

	override fun onRecyclerViewScrolled(lastVisiblePos: Int, totalPosCount: Int) {
		//Log.d(TAG, "onRecyclerViewScrolled")
		//Log.d(TAG, "lastVisiblePos=$lastVisiblePos, size= ${setlistsRepository.getSetlists().size}")
		if (lastVisiblePos>=setlistsRepository.getSetlists().size-1) {
  			addMoreSetlists(setlistsRepository.getCurrentArtist()!!.mbid, object: AnswerListener<List<Setlist>> {
				override fun getAnswer(t: List<Setlist>) {
					getView()?.showSetlistList(t)
				}
			})
		}
	}


	override fun viewIsReady() {
		val artist = setlistsRepository.getCurrentArtist()
		if (artist!=null) {
			searchSetlists(artist.mbid, object : AnswerListener<List<Setlist>> {
				override fun getAnswer(t: List<Setlist>) {
					getView()?.showSetlistList(t)
					if (t.isEmpty()) {
						getView()?.showToast("сетлистов для ${artist.name} не найдено")
					}
				}
			})
		}
    }

    init {
		this.setlistsRepository = setlistsRepository
    }

	private fun searchSetlists(artistMbid: String, answerListener: AnswerListener<List<Setlist>>) {
		val handler = object : Handler(Looper.getMainLooper()) {
			override fun handleMessage(msg: Message) {
				answerListener.getAnswer(setlistsRepository.getSetlists())
			}
		}

		val runnable = Runnable {
			val setlists = ArrayList<Setlist>()
			kotlin.run {
				setlistsRepository.searchSetlists(artistMbid).forEach {
					setlists.add(it)
				}
				setlistsRepository.setNewSetlists(setlists)
				handler.sendEmptyMessage(1)
			}
		}

		val thread = Thread(runnable)
		thread.start()
	}

	private fun addMoreSetlists(artistMbid: String, answerListener: AnswerListener<List<Setlist>>) {
		val handler = object : Handler(Looper.getMainLooper()) {
			override fun handleMessage(msg: Message) {
				answerListener.getAnswer(setlistsRepository.getSetlists())
				setlistsPage++
			}
		}

		val runnable = Runnable {
			val setlists = ArrayList<Setlist>()
			kotlin.run {
				setlistsRepository.searchSetlists(artistMbid, setlistsPage).forEach {
					setlists.add(it)
				}
				setlistsRepository.addToSetlists(setlists)
				handler.sendEmptyMessage(1)
			}
		}

		val thread = Thread(runnable)
		thread.start()
	}


}