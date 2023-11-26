import { initializeApp } from 'firebase/app'
import { addDoc, collection, deleteDoc, doc } from 'firebase/firestore'
import { useFirestore, useCollection } from 'vuefire'

export const firebaseApp = initializeApp({
  apiKey: 'AIzaSyAz2qnkvvcTseFkisdIoXKXTDQ5uYN-PNM',
  authDomain: 'agenda-vue-9056f.firebaseapp.com',
  projectId: 'agenda-vue-9056f',
  storageBucket: 'agenda-vue-9056f.appspot.com',
  messagingSenderId: '685604691859',
  appId: '1:685604691859:web:c933347b7be1ddc7dd5266',
  measurementId: 'G-RT7P01PQM5'
})

const db = useFirestore()

export const contacts = useCollection(collection(db, 'contacts'))

export async function deleteContact(id: string) {
  return await deleteDoc(doc(db, 'contacts', id))
}

export async function addContact(name: string, email: string, phone: string) {
  return await addDoc(collection(db, 'contacts'), { name, email, phone })
}
