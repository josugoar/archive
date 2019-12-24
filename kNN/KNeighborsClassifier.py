import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
from scipy.stats import mode

class KNeighborsClassifier:
    """
    Classifier implementing the k-nearest neighbors vote

    ## Parameters
    - n_neighbors: {int, optional (default = 5)} Number of neighbors to use by default for queries
    """

    # KNeighborsClassifier.fit() method flag
    fit_called = False

    def __new__(cls, n_neighbors=5):
        # Assert data type
        assert isinstance(n_neighbors, int), 'Data Type Error: n_neighbors dtype must be integer'
        return super(KNeighborsClassifier, cls).__new__(cls)

    def __init__(self, n_neighbors=5):
        self.n_neighbors = n_neighbors

    def __str__(self):
        # Return class atributes
        return f'KNeighborsClassifier\n- n_neighbors: {self.n_neighbors}'

    def fit(self, X, y):
        """
        Fit the model using X as training data and y as target values

        ## Parameters
        - X: {array} Training data of shape [n_samples, m_features]
        - y: {array} Target values of shape [n_samples,]
        """

        # Assert array dimensions
        assert X.ndim == 2, 'Dimension Error: X.shape must be two-dimensional'
        assert y.ndim == 1, 'Dimension Error: y.shape must be one-dimensional'
        assert X.shape[0] == y.size, 'Dimension Error: X and y shapes must respectively be of [n_samples, m_features] and [n_samples,]'
        assert self.n_neighbors > X.shape[0], 'Dimension Error: n_neighbors must not be larger than X n_rows'

        # Activate KNeighborsClassifier.fit() method flag
        KNeighborsClassifier.fit_called = True

        # Fit X and y to class
        self.__X = X
        self.__y = y

    def predict(self, X):
        """
        Predict the class labels for the provided data

        ## Parameters
        - X: {array} Input data of shape [n_query, m_features]

        ## Returns
        - y: {array} Class labels for each data sample of shape [n_samples,]
        """

        # Assert method and dimensions
        assert KNeighborsClassifier.fit_called, 'Method Error: KNeighborsClassifier.fit() method must first be called'
        assert X.ndim == 2, 'Dimension Error: X.shape must be two-dimensional'
        assert self.__X.shape[1] == X.shape[1], 'Dimension Error: fit X and input X shapes must respectively be of [n_samples, m_features] and [n_query, m_features]'

        # Fit point to point k distances an indices
        self.__distances, self.__indices = self.kneighbors(X)
        # Set point labels
        self.__labels = np.array([mode(self.__y[indice])[0][0] for indice in self.__indices])

        return self.__labels

    def kneighbors(self, X, return_distances=True):
        """
        Finds the K-neighbors of a point and returns indices of and distances to the neighbors of each point

        ## Parameters
        - X: {array} Input data of shape [n_query, m_features]
        - return_distance: {boolean, optional (default = True)} If False, distances will not be returned

        ## Returns
        - dist: {array} Lengths to points
        - ind: {array} Indices of the nearest points
        """

        # Assert method
        assert KNeighborsClassifier.fit_called, 'Method Error: KNeighborsClassifier.fit() method must first be called'

        # Transform to three dimensions
        X = X[:, :, np.newaxis]

        # Calculate point to point distances
        dist = np.sqrt((np.power(X - self.__X.T, 2)).sum(axis=1))

        # Calculate point to point k indices
        kindices = np.argsort(dist, axis=1)[:, 0:self.n_neighbors]

        if return_distances == True:
            # Calculate point to point k distances
            kdistances = np.sort(dist, axis=1)[:, 0:self.n_neighbors]
            return kdistances, kindices
        return kindices

    def score(self, X, y_true):
        """
        Returns the mean accuracy on the given test data and labels

        ## Parameters
        - y_pred: {array} Predicted values of X of shape [n_samples,]
        - y_fit: {array} True labels of X of shape [n_samples,]

        ## Returns
        - score: {float} Mean accuracy
        """

        # Assert dimensions
        assert X.shape[0] == y_true.size, 'Dimension Error: X and y shapes must respectively be of [n_query, m_features] and [n_query,]'

        y_pred = self.predict(X)

        # Calculate mean score
        return np.sum(y_pred == y_true)/y_true.size

    def kneighbors_2Dgraph(self, X):
        """
        Computes the graph of k-Neighbors for points in X

        ## Parameters
        - X: {array} Input data of shape [n_query, m_features]

        ## Returns
        - fig: matplotlib figure containing points and decision boundaries
        """

        # Assert dimension
        assert X.shape[1] == 2, 'Dimension Error: X must not have more than two features'

        # Predict labels
        labels = self.predict(X)

        # Plot parameters
        plt.style.use('seaborn')
        plt.title('kneighbors_2Dgraph')
        plt.xlabel('feature_1')
        plt.ylabel('feature_2')
        plt.xlim(self.__X[:, 0].min(), self.__X[:, 0].max())
        plt.ylim(self.__X[:, 1].min(), self.__X[:, 1].max())

        # Make grid
        xx, yy = np.meshgrid(np.linspace(self.__X[:, 0].min(), self.__X[:, 0].max(), num=200),
                             np.linspace(self.__X[:, 1].min(), self.__X[:, 1].max(), num=200))
        Z = self.predict(np.c_[xx.ravel(), yy.ravel()]).reshape(xx.shape)

        # Plot contourn
        contours = plt.contourf(xx, yy, Z,
                                alpha=0.3,
                                levels=np.arange(np.unique(self.__y).size + 1) - 0.5)

        # Plot points
        for label in np.unique(self.__y):

            # True labels
            mask_true = np.argwhere(self.__y == label)
            fit_point = plt.scatter(self.__X[mask_true, 0], self.__X[mask_true, 1],
                                    alpha=0.5)

            # Predicted labels
            mask_pred = np.argwhere(labels == label)
            pred_point = plt.scatter(X[mask_pred, 0], X[mask_pred, 1],
                                     color=fit_point.get_facecolor()[0],
                                     alpha=1)

        # Show plot
        plt.show()
