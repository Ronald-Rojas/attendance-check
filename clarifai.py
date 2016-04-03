from clarifai_basic import ClarifaiCustomModel

clarifai = ClarifaiCustomModel()
concept_name = 'ronald'
Ronald_Positives = [
    '/path/to/pictues/of/me',
    '/path/to/more/pictures/of/me',
    '/even/more/pictues]'
    ]

for positives in Ronald_Positives:
    clarifai.positive(positives, concept_name)


Ronald_Negatives = [
    '/path/to/pictues/of/not/me',
    '/path/to/more/pictures/of/me',
    '/even/more/pictues'
    ]

for negatives in Ronald_Negatives:
    clarifai.negative( negatives, concept_name)

clarifia.train(concept_name)


result = clarifai.predict('/path/to/picture/taken', concept_name)

print result['urls'][0]['score']


