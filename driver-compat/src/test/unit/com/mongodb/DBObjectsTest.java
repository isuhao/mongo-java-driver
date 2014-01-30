/*
 * Copyright (c) 2008 - 2014 MongoDB Inc. <http://mongodb.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mongodb;

import org.junit.Test;
import org.mongodb.Document;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class DBObjectsTest {
    @Test
    public void shouldCreateADocumentWithTheSameSimpleValuesFromADBObject() {
        DBObject dbObject = createBasicDBObject();

        Document actualDocument = DBObjects.toDocument(dbObject);

        Document expectedDocument = createDocument();

        assertThat(actualDocument, is(expectedDocument));
    }

    @Test
    public void shouldCreateADBObjectWithTheSameSimpleValuesFromADocument() {
        Document document = createDocument();

        DBObject actualDBObject = DBObjects.toDBObject(document);

        DBObject expectedDBObject = createBasicDBObject();

        assertThat(actualDBObject, is(expectedDBObject));
    }

    @Test
    public void shouldHandleNestedDocumentsWhenConvertingDocumentToDBObject() {
        BasicDBObject dbObject = createBasicDBObject();
        dbObject.append("subDocument", createBasicDBObject());

        Document actualDocument = DBObjects.toDocument(dbObject);

        Document expectedDocument = createDocument();
        expectedDocument.append("subDocument", createDocument());

        assertThat(actualDocument, is(expectedDocument));
    }

    @Test
    public void shouldHandleNestedDocumentsWhenConvertingDBObjectToDocument() {
        Document document = createDocument();
        document.append("subDocument", createDocument());

        BasicDBObject actualDBObject = DBObjects.toDBObject(document);

        BasicDBObject expectedDBObject = createBasicDBObject();
        expectedDBObject.append("subDocument", createBasicDBObject());

        assertThat(actualDBObject, is(expectedDBObject));
    }

    @Test
    public void shouldHandleComplexTypesWhenConvertingDBObjectToDocument() {
        BasicDBObject dbObject = createBasicDBObject();
        dbObject.append("complexType", createNewTestObject());

        Document actualDocument = DBObjects.toDocument(dbObject);

        Document expectedDocument = createDocument();
        expectedDocument.append("complexType", createNewTestObject());

        assertThat(actualDocument, is(expectedDocument));
    }

    @Test
    public void shouldHandleNullsWhenConvertingDBObjectToDocument() {
        BasicDBObject dbObject = createBasicDBObject();
        dbObject.append("myNullValue", null);

        Document actualDocument = DBObjects.toDocument(dbObject);

        Document expectedDocument = createDocument();
        expectedDocument.append("myNullValue", null);

        assertThat(actualDocument, is(expectedDocument));
    }

    @Test
    public void shouldHandleNullsWhenConvertingDocumentToDBObject() {
        Document document = createDocument();
        document.append("myNullValue", null);

        BasicDBObject actualDBObject = DBObjects.toDBObject(document);

        BasicDBObject expectedDBObject = createBasicDBObject();
        expectedDBObject.append("myNullValue", null);

        assertThat(actualDBObject, is(expectedDBObject));
    }

    @Test
    public void shouldHandleListsWhenConvertingDBObjectToDocument() {
        BasicDBObject dbObject = createBasicDBObject();
        dbObject.append("listOfInts", asList(7843, 75439, 57489, 547));

        Document actualDocument = DBObjects.toDocument(dbObject);

        Document expectedDocument = createDocument();
        expectedDocument.append("listOfInts", asList(7843, 75439, 57489, 547));

        assertThat(actualDocument, is(expectedDocument));
    }

    @Test
    public void shouldHandleListsWhenConvertingDocumentToDBObject() {
        Document document = createDocument();
        document.append("listOfInts", asList(7843, 75439, 57489, 547));

        BasicDBObject actualDBObject = DBObjects.toDBObject(document);

        BasicDBObject expectedDBObject = createBasicDBObject();
        expectedDBObject.append("listOfInts", asList(7843, 75439, 57489, 547));

        assertThat(actualDBObject, is(expectedDBObject));
    }

    //TODO: tests around recursive structures

    private BasicDBObject createBasicDBObject() {
        BasicDBObject dbObject = new BasicDBObject();
        dbObject.append("IntValue", 8563838);
        dbObject.append("StringValue", "Something");
        dbObject.append("LongValue", 5748932L);
        dbObject.append("DoubleValue", 3.14);
        return dbObject;
    }

    private Document createDocument() {
        Document document = new Document();
        document.append("IntValue", 8563838);
        document.append("StringValue", "Something");
        document.append("LongValue", 5748932L);
        document.append("DoubleValue", 3.14);
        return document;
    }

    private MyTestObject createNewTestObject() {
        MyTestObject testObject = new MyTestObject();
        testObject.myStringField = "I'm a String";
        testObject.myIntField = 45;
        testObject.myDoubleField = 837.8675;
        return testObject;
    }

    private static class MyTestObject {
        private String myStringField;
        private int myIntField;
        private double myDoubleField;

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            MyTestObject that = (MyTestObject) o;

            if (Double.compare(that.myDoubleField, myDoubleField) != 0) {
                return false;
            }
            if (myIntField != that.myIntField) {
                return false;
            }
            if (!myStringField.equals(that.myStringField)) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            int result;
            long temp;
            result = myStringField.hashCode();
            result = 31 * result + myIntField;
            temp = myDoubleField != +0.0d ? Double.doubleToLongBits(myDoubleField) : 0L;
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            return result;
        }
    }

}