import { initializeApp } from 'firebase/app'
import { addDoc, collection, deleteDoc, doc } from 'firebase/firestore'
import { useFirestore, useCollection } from 'vuefire'
// @ts-ignore
import firebaseConfig from '/firebaseConfig'

export const firebaseApp = initializeApp(firebaseConfig)

const db = useFirestore()

export const contacts = useCollection(collection(db, 'contacts'))

export async function deleteContact(id: string) {
  return await deleteDoc(doc(db, 'contacts', id))
}

export async function addContact(name: string, email: string, phone: string) {
  return await addDoc(collection(db, 'contacts'), { name, email, phone })
}
