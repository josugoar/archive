import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns

class KNeighborsClassifier:
    """
    Classifier implementing the k-nearest neighbors vote

    ## Parameters
    - n_neighbors: {int, optional (default = 5)} Number of neighbors to use by default for queries
    """

    # KNeighborsClassifier.fit() method flag
    fit_called = False

    def __new__(cls, n_neighbors=5):
        # Assert data type and value
        assert isinstance(n_neighbors, int), 'Data Type Error: n_neighbors dtype must be integer'
        assert n_neighbors > 0, 'Value Error: n_neighbors must be a positive integer'

        # Create instance
        instance = super(KNeighborsClassifier, cls).__new__(cls)

        return instance

    def __init__(self, n_neighbors=5):
        self.n_neighbors = n_neighbors

    def __str__(self):
        # Return class atributes
        return f'KNeighborsClassifier\n  n_neighbors: {self.n_neighbors}'

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
        assert X.shape[0] > self.n_neighbors, 'Dimension Error: n_neighbors must not be larger than X n_rows'

        # Activate KNeighborsClassifier.fit() method flag
        KNeighborsClassifier.fit_called = True

        # Fit X and y to class
        self.__X_fit = X
        self.__y_fit = y

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
        assert self.__X_fit.shape[1] == X.shape[1], 'Dimension Error: fit X and input X shapes must respectively be of [n_samples, m_features] and [n_query, m_features]'

        # Fit point to point k distances an indices
        indices = self.kneighbors(X)
        # Find most common label
        labels = np.array([np.unique(self.__y_fit[indice])[0] for indice in indices])

        return labels

    def kneighbors(self, X, return_distances=False):
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
        dist = np.sqrt((np.power(X - self.__X_fit.T, 2)).sum(axis=1))

        # Calculate point to point k indices
        kindices = np.argsort(dist, axis=1)[:, :self.n_neighbors]

        if return_distances:
            # Calculate point to point k distances
            kdistances = np.sort(dist, axis=1)[:, :self.n_neighbors]
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

        # Calculate mean score
        score = np.sum(self.predict(X) == y_true) / y_true.size

        return score

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

        # Plot style
        plt.style.use('seaborn')

        # Labels
        labels_pred = self.predict(X)
        labels_true = np.unique(self.__y_fit)

        # Plot points
        for label in labels_true:

            # True labels
            mask_true = np.argwhere(self.__y_fit == label)
            fit_point = plt.scatter(self.__X_fit[mask_true, 0], self.__X_fit[mask_true, 1],
                                    alpha=0.5)

            # Predicted labels
            mask_pred = np.argwhere(labels_pred == label)
            pred_point = plt.scatter(X[mask_pred, 0], X[mask_pred, 1],
                                     color=fit_point.get_facecolor()[0],
                                     alpha=1)

        # Make grid
        num = 200
        xx, yy = np.meshgrid(np.linspace(*plt.xlim(), num),
                             np.linspace(*plt.ylim(), num))
        Z = self.predict(np.c_[xx.ravel(), yy.ravel()]).reshape(xx.shape)

        # Plot contourn
        contours = plt.contourf(xx, yy, Z,
                                alpha=0.25,
                                levels=np.arange(labels_true.size + 1) - 0.5,
                                cmap='rainbow')

        # Plot parameters
        plt.title('kneighbors_2Dgraph')
        plt.xlabel('feature_1')
        plt.ylabel('feature_2')

        # Show plot
        plt.show()
